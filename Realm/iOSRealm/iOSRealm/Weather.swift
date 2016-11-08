//
//  Weather.swift
//  iOSRealm
//
//  Created by Niels Bokmans on 08/11/16.
//  Copyright © 2016 Niels Bokmans. All rights reserved.
//

import Foundation
import RealmSwift

class Weather : Object {
    dynamic var date = ""
    dynamic var forecast = ""
    dynamic var humidity = ""
    dynamic var wind: Wind?
}
	
class Wind: Object {
    dynamic var direction = ""
    dynamic var speed = ""
}