//
//  WeatherModel.swift
//  SQLite2
//
//  Created by Coen Smid on 09-11-16.
//  Copyright © 2016 Coen Smid. All rights reserved.
//

import Foundation

class WeatherModel
{

    var db: COpaquePointer = nil
    let WIND_TABLE = "wind"
    let WEATHER_TABLE = "weather"
    let WEATHER_DATE = "date"
    let WEATHER_FORECAST = "forecast"
    let WEATHER_HUMIDITY = "humity"
    let WEATHER_WIND = "wind"
    let WIND_SPEED = "speed"
    let WIND_DIRECTION = "direction"
    
    
    func WeatherModel()
    {
        createDatabase()
    }
    
    
    private func createDatabase()
    {
        
        let fileName = "Weather.sqlite"
        
        let file = try! NSFileManager.defaultManager().URLForDirectory(.DocumentDirectory, inDomain: .UserDomainMask, appropriateForURL: nil, create: true).URLByAppendingPathExtension(fileName)?.path
        
        
        sqlite3_open_v2(file!, &db, SQLITE_OPEN_CREATE | SQLITE_OPEN_READWRITE, nil);
        
        sqlite3_exec(db, "DROP TABLE WEATHER", nil,nil,nil)
        sqlite3_exec(db, "DROP TABLE WIND", nil,nil,nil)
        
        let createWindTable = "CREATE TABLE \(WIND_TABLE) (id INTEGER PRIMARY KEY AUTOINCREMENT, \(WIND_SPEED) TEXT, \(WIND_DIRECTION) TEXT )"
        let createWeatherTable = "CREATE TABLE \(WEATHER_TABLE) (id INTEGER PRIMARY KEY AUTOINCREMENT, \(WEATHER_DATE) TEXT, \(WEATHER_FORECAST) TEXT, \(WEATHER_HUMIDITY) TEXT, \(WEATHER_WIND) INTEGER, FOREIGN KEY(\(WEATHER_WIND)) REFERENCES \(WIND_TABLE)(id))"
        
        if sqlite3_exec(db, createWindTable, nil, nil, nil) != SQLITE_OK
        {
            let errmsg = String(UTF8String: sqlite3_errmsg(db))
            print("error creating table: \(errmsg)")
        }
        if sqlite3_exec(db, createWeatherTable, nil, nil, nil) != SQLITE_OK
        {
            let errmsg = String(UTF8String: sqlite3_errmsg(db))
            print("error creating table: \(errmsg)")
        }
    }
    
    private func getSingleWeatherData() -> String
    {
        let queryStatementString = "SELECT w.\(WEATHER_DATE), w.\(WEATHER_FORECAST), w.\(WEATHER_HUMIDITY), wi.\(WIND_SPEED), wi.\(WIND_DIRECTION) FROM \(WEATHER_TABLE) w INNER JOIN \(WIND_TABLE) ON w.\(WEATHER_WIND)=wi.id "
        var queryStatement: COpaquePointer = nil
        
        if sqlite3_prepare_v2(db, queryStatementString, -1, &queryStatement, nil) == SQLITE_OK
        {
            
            if sqlite3_step(queryStatement) == SQLITE_ROW {
            
                let date = String.fromCString(UnsafePointer<CChar>(sqlite3_column_text(queryStatement, 0)))!
                let forecast = String.fromCString(UnsafePointer<CChar>(sqlite3_column_text(queryStatement, 1)))!
                let humidity = String.fromCString(UnsafePointer<CChar>(sqlite3_column_text(queryStatement, 2)))!
                let speed = String.fromCString(UnsafePointer<CChar>(sqlite3_column_text(queryStatement, 3)))!
                let direction = String.fromCString(UnsafePointer<CChar>(sqlite3_column_text(queryStatement, 4)))!
                
                print("\(date) | \(forecast) | \(humidity) | \(speed) | \(direction)")
                
            }
            else
            {
                print("Query returned no results")
            }
        }
        else
        {
            print("SELECT statement could not be prepared bleip")
        }
        
        sqlite3_finalize(queryStatement)
        
    
        return "No results found"
    }
    
    
    func closeDatabase()
    {
        sqlite3_close(db)
    }
    
    func insertExampleData()
    {
        if sqlite3_exec(db, "insert into \(WIND_TABLE) (\(WIND_SPEED), \(WIND_DIRECTION)) values ('North' , 5)", nil, nil, nil) != SQLITE_OK
        {
            let errmsg = String(UTF8String: sqlite3_errmsg(db))
            print("error inserting data: \(errmsg)")
        }
        else
        {
            let idQuery = "SELECT LAST_INSERT_ROWID()"
            var queryStatement: COpaquePointer = nil
            if sqlite3_prepare_v2(db, idQuery, -1, &queryStatement, nil) == SQLITE_OK {
                
                if sqlite3_step(queryStatement) == SQLITE_ROW {
                    
                    let id = sqlite3_column_int(queryStatement, 0)
                    if sqlite3_exec(db, "insert into \(WEATHER_TABLE) (\(WEATHER_DATE), \(WEATHER_FORECAST), \(WEATHER_WIND), \(WEATHER_WIND)) values ('8-11-2016', 'Sunny', '20%', \(id) )", nil, nil, nil) != SQLITE_OK {
                        let errmsg = String(UTF8String: sqlite3_errmsg(db))
                        print("error inserting data: \(errmsg)")
                    }
                }
            }
             sqlite3_finalize(queryStatement)
        }
    }
}
