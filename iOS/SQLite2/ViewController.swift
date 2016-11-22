//
//  ViewController.swift
//  SQLite2
//
//  Created by Coen Smid on 06-11-16.
//  Copyright © 2016 Coen Smid. All rights reserved.
//

import UIKit

class ViewController: UIViewController
{

    override func viewDidLoad()
    {
        let model = WeatherModel()
        model.insertExampleData()
        
        model.getWeatherData()
        
        model.closeDatabase()
    }
    
    
    
   
}

