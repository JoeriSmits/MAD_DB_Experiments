//
//  WeatherTableViewController.swift
//  iOSRealm
//
//  Created by Niels Bokmans on 08/11/16.
//  Copyright © 2016 Niels Bokmans. All rights reserved.
//

import UIKit
import RealmSwift
class WeatherTableViewController : UITableViewController {
    
    var weatherData = [Weather]()
    
    private var realm: Realm!
    
    let authURL: NSURL = NSURL(string: "http://185.14.186.238:9080")!
    let realmURL: NSURL = NSURL(string: "realm://185.14.186.238:9080/~/newRealm")!
    let username = "robaben11@gmail.com"
    let password = "ditisrealm"

    override func viewDidLoad() {
        super.viewDidLoad()
        try! synchronouslyLogInUser()
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
        weatherData = Array(realm.objects(Weather))
        print(weatherData)
        dispatch_async(dispatch_get_main_queue(), { () -> Void in
            self.tableView.reloadData()
        })
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cellIdentifier = "WeatherTableViewCell"
        let cell = tableView.dequeueReusableCellWithIdentifier(cellIdentifier, forIndexPath: indexPath) as! WeatherTableViewCell
        cell.weather.text = weatherData[indexPath.row].description
        print(cell.weather.text)
        return cell
    }
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return weatherData.count
    }
    
    
}
