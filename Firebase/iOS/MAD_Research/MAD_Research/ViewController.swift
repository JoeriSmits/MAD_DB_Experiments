//
//  ViewController.swift
//  MAD_Research
//
//  Created by Joeri Smits on 31-10-16.
//  Copyright © 2016 Joeri Smits. All rights reserved.
//

import UIKit
import FirebaseDatabase

class ViewController: UIViewController {

    var ref: FIRDatabaseReference!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        ref = FIRDatabase.database().reference()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func writeData(_ sender: UIButton) {
        
        self.ref.child("weatherForecast").setValue("Hallow");
    }
}

