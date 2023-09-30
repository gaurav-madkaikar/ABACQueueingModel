#include <bits/stdc++.h>
using namespace std;

// Data Structures
// Store the ABAC policy
map<string, map<string, map<string, string>>> abac_policy;

// Store subject and object information
map<string, map<string, string>> user_info, object_info;

// Access Control Matrix
map<string, string> ACMatrix;



void preprocess()
{
    cout << "------------------ PREPROCESSING PART ------------------" << endl;

    
    return;
}

int main()
{
    string welcomeMessage(45, '+');
    welcomeMessage = " Welcome to the ABAC Policy Generator " + welcomeMessage;
    cout << welcomeMessage << endl;

    // Generate the test data
    preprocess();

    return 0;
}