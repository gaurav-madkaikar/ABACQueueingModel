import json
import math
import graphviz
from pprint import pprint
from time import sleep


class NPolTreeNode:
    def __init__(
        self, attr_type, attribute, branches=None, decision=None, is_leaf=False
    ):
        self._id = str(id(self))
        self.attr_type = attr_type
        self.attribute = attribute
        self.branches = branches if branches else {}
        self.decision = decision
        self.is_leaf = is_leaf
    
    def __repr__(self):
        return f"Node: ({self.attribute}, {self.decision}), IsLeaf: {self.is_leaf}, Branches: {list(self.branches.keys())}"


class NPolTree:
    def __init__(self, policy: str | dict):
        self.update_policy(policy)

    def update_policy(self, policy):
        if type(policy) == str:
            self.policy: dict = json.loads(policy)
        else:
            self.policy: dict = policy
        # pprint(f"Policy: {self.policy}")
        if len(self.policy) == 0:
            self.root = NPolTreeNode(None, None, decision="Deny", is_leaf=True)
            return
        self.stats = self.calculate_stats(self.policy)
        # pprint(f"Stats: {self.stats}")
        self.attributes = self.gather_attributes()
        # pprint(f"Attributes: {self.attributes}")
        self.attr_vals = self.gather_attr_vals()
        # pprint(f"Attribute values: {self.attr_vals}")
        self.root = self.build_tree(self.policy, self.attributes)

    def gather_attributes(self):
        attr_type_attrs = []
        for attr_type, attrs in self.stats.items():
            for attr in attrs:
                attr_type_attrs.append((attr_type, attr))
        return attr_type_attrs

    def gather_attr_vals(self):
        attr_vals = {}
        for attr_type, attrs in self.stats.items():
            for attr, values in attrs.items():
                if attr_type not in attr_vals:
                    attr_vals[attr_type] = {}
                # Exclude wildcard "*" from attribute values
                attr_vals[attr_type][attr] = values.keys() 
        return attr_vals

    def calculate_stats(self, policy_rules):
        stats = {"sub": {}, "obj": {}, "op": {"op": {}}}
        for rule_name, rule in policy_rules.items():
            subj_attrs = rule.get("sub", {})
            obj_attrs = rule.get("obj", {})
            op_values = rule.get("op", [])
            for attr, values in subj_attrs.items():
                if attr not in stats["sub"]:
                    stats["sub"][attr] = {}
                for value in values:
                    if value not in stats["sub"][attr]:
                        stats["sub"][attr][value] = 0
                    stats["sub"][attr][value] += 1
            for attr, values in obj_attrs.items():
                if attr not in stats["obj"]:
                    stats["obj"][attr] = {}
                for value in values:
                    if value not in stats["obj"][attr]:
                        stats["obj"][attr][value] = 0
                    stats["obj"][attr][value] += 1
            if op_values not in stats["op"]["op"]:
                stats["op"]["op"][op_values] = 0
            stats["op"]["op"][op_values] += 1
        return stats

    def entropy(self, attr_type, attribute, rules: dict[str, dict]) -> float:
        values = self.attr_vals[attr_type][attribute]
        counts = {value: 0 for value in values}
        total = 0
        for rule_name, rule in rules.items():
            rule_values = (
                rule.get(attr_type, {}).get(attribute, [])
                if attr_type != "op"
                else [rule.get(attr_type, None)]
            )
            if "*" in rule_values:
                continue  # Skip rules with wildcard in entropy calculation
            total += 1
            for value in rule_values:
                counts[value] += 1
        entropy = 0
        for value, count in counts.items():
            probability = count / total if total > 0 else 0
            entropy -= probability * math.log2(probability) if probability > 0 else 0
        return entropy

    def select_attribute(self, attributes, rules: dict[str, dict]) -> tuple:
        max_entropy = float("-inf")
        selected_attribute = (None, None)
        for attr_type, attribute in attributes:
            entropy = self.entropy(attr_type, attribute, rules)
            if entropy > max_entropy:
                max_entropy = entropy
                selected_attribute = (attr_type, attribute)
        return selected_attribute

    def build_tree(self, policy_rules, attributes):
        # print(f"Number of rules: {len(policy_rules)}")
        # print(f"Number of attributes: {len(attributes)}")
        if not policy_rules:
            return NPolTreeNode(None, None, decision="Deny", is_leaf=True)
        if len(attributes) == 0:
            return NPolTreeNode(None, None, decision="Allow", is_leaf=True)
        attr_type, attribute = self.select_attribute(attributes, policy_rules)
        if attr_type is None:
            return NPolTreeNode(None, None, decision="Allow", is_leaf=True)
        # print(f"Selected attribute: {attr_type}, {attribute}")
        node = NPolTreeNode(attr_type, attribute)
        for value in self.attr_vals[attr_type][attribute]:
            new_attributes = [
                attr for attr in attributes if attr != (attr_type, attribute)
            ]
            if attr_type == "op":
                new_policy_rules = {
                    rule_name: rule
                    for rule_name, rule in policy_rules.items()
                    if value == rule.get(attr_type, None) or rule.get(attr_type, None) == "*"
                }
            else:
                new_policy_rules = {
                    rule_name: rule
                    for rule_name, rule in policy_rules.items()
                    if value in rule.get(attr_type, {}).get(attribute, []) or "*" in rule.get(attr_type, {}).get(attribute, [])
                }
            # print(f"Branched on: {attr_type}, {attribute}, {value}")
            # print(f"Number of new rules: {len(new_policy_rules)}")
            if len(new_policy_rules) == 0: # Skip empty branches
                continue
            node.branches[value] = self.build_tree(new_policy_rules, new_attributes)
        return node

    def _display_tree(self, node, graph: graphviz.Digraph, parent=None, value=None):
        if node.is_leaf:
            graph.node(node._id, label=node.decision)
            if parent:
                graph.edge(parent._id, node._id, label=value)
        else:
            graph.node(node._id, label=node.attribute)
            if parent:
                graph.edge(parent._id, node._id, label=value)
            for value, branch in node.branches.items():
                self._display_tree(branch, graph, node, value)

    def display_tree(self):
        graph = graphviz.Digraph()
        self._display_tree(self.root, graph)
        graph.render("policy_tree", view=True)
        
    def _print_tree(self, node, depth=0):
        if node.is_leaf:
            print(f"{'  ' * depth}Decision: {node.decision}")
        else:
            print(f"{'  ' * depth}Attribute: {node.attribute}")
            for value, branch in node.branches.items():
                print(f"{'  ' * depth}Value: {value}")
                self._print_tree(branch, depth + 1)
    
    def print_tree(self):
        self._print_tree(self.root)
    
    def _resolve(self, node, request, depth=0):
        # print(f"{'    ' * depth}{node}")
        if node.is_leaf:
            return (node.decision == "Allow")
        attr_type = node.attr_type
        attribute = node.attribute
        access = False
        
        for value in (request.get(attr_type, {}).get(attribute, []) if attr_type != "op" else [request.get(attr_type, None)]):
            if value in node.branches:
                # print(f"Value: {value:>8}", end=" ")
                access |= self._resolve(node.branches[value], request, depth + 1)
                if access:
                    break
        if len(value) and "*" not in value:
            if "*" in node.branches:
                # print(f"Value: {'*':>8}", end=" ")
                access |= self._resolve(node.branches["*"], request, depth + 1)
        return access
    
    def resolve(self, request):
        # print(f"Request: {request}")
        return "Allow" if self._resolve(self.root, request) else "Deny"
    
    def insert_rule(self, rule_name, rule):
        print(f"Inserting rule: {rule_name}: {rule}")
        print(f"Prev policy size: {len(self.policy)}")
        self.policy[rule_name] = rule
        self.update_policy(self.policy)


# Example usage
if __name__ == "__main__":
    # Example policy string (you can replace this with any policy)
    policy_str = ""
    with open('./policy.json', 'r') as f:
        policy_str = f.read()
    policy = json.loads(policy_str)
    pol_tree = NPolTree({})
    for i in range(1, 5):
        try:
            rule_name = "rule_" + str(i)
            rule = policy[rule_name]
            pprint(f"Inserting rule: {rule_name}: {rule}")
            pol_tree.insert_rule(rule_name, rule)
            if i>=3:
                pol_tree.display_tree()
                sleep(1000)
        except KeyboardInterrupt:
            continue
        
    
    # pol_tree.print_tree()
    
    
