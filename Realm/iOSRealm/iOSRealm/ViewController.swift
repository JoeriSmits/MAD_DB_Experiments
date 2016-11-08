//
//  ViewController.swift
//  iOSRealm
//
//  Created by Niels Bokmans on 02/11/16.
//  Copyright © 2016 Niels Bokmans. All rights reserved.
//

import UIKit
import RealmSwift

class ViewController: UIViewController {

    private var realm: Realm!
    
    let authURL: NSURL = NSURL(string: "http://185.14.186.238:9080")!
    let realmURL: NSURL = NSURL(string: "realm://185.14.186.238:9080/~/noID")!
    let username = "robaben11@gmail.com"
    let password = "ditisrealm"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        try! synchronouslyLogInUser()
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    private func synchronouslyLogInUser() throws {
        SyncUser.authenticateWithCredential(Credential.usernamePassword(username, password: password, actions:.UseExistingAccount), authServerURL: authURL) { (user, error) in
            if let user = user {
                self.setDefaultRealmConfiguration(user)
            }
        }
    }
    
    private func setDefaultRealmConfiguration(user: SyncUser) {
        Realm.Configuration.defaultConfiguration = Realm.Configuration(syncConfiguration: (user, realmURL), objectTypes: [Weather.self, Wind.self])
        realm = try! Realm()
    }
    
    private func writeToRealm() {
    }

}

