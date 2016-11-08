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
    @IBOutlet weak var textView: UITextView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        ref = FIRDatabase.database().reference()
        
        _ = ref.observe(FIRDataEventType.value, with: { (snapshot) in
            let postDict = snapshot.value as? [String : AnyObject] ?? [:]
            
            self.textView.text = postDict.description
        })
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func writeData(_ sender: UIButton) {
        let wind: [String: String] = ["direction": "280", "speed": "10 knots"]
        let weather: [String: Any] = ["date": "2016-10-26", "forecast": "Sunny", "humidity": "80%", "wind": wind]
        
        self.ref.child("weatherForecast").childByAutoId().setValue(weather)
    }
}
