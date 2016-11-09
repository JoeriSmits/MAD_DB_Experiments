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
        openData()
    }
    
    private func openData()
    {
        var db: COpaquePointer = nil
        let fileName = "Weather.sqlite"
        
        do
        {
            let file = try NSFileManager.defaultManager().URLForDirectory(.DocumentDirectory, inDomain: .UserDomainMask, appropriateForURL: nil, create: true).URLByAppendingPathExtension(fileName)?.path
            
            sqlite3_open_v2(file!, &db, SQLITE_OPEN_CREATE | SQLITE_OPEN_READWRITE, nil);
            
            sqlite3_exec(db, "DROP TABLE WEATHER", nil,nil,nil)
            sqlite3_exec(db, "DROP TABLE WIND", nil,nil,nil)
            
            let createWindTable = "CREATE TABLE wind (id INTEGER PRIMARY KEY AUTOINCREMENT, speed TEXT, direction TEXT )"
            let createWeatherTable = "CREATE TABLE weather (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, forecast TEXT, humidity TEXT, wind INTEGER, FOREIGN KEY(wind) REFERENCES wind(id))"
            
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
            
    
            if sqlite3_exec(db, "insert into wind (speed, direction) values ('North' , 5)", nil, nil, nil) != SQLITE_OK
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
                        if sqlite3_exec(db, "insert into weather (date, forecast, humidity, wind) values ('8-11-2016', 'Sunny', '20%', \(id) )", nil, nil, nil) != SQLITE_OK {
                            let errmsg = String(UTF8String: sqlite3_errmsg(db))
                            print("error inserting data: \(errmsg)")
                        }
                    }
                }
            }
            

            let queryStatementString = "SELECT * FROM weather;"
            var queryStatement: COpaquePointer = nil
            
            if sqlite3_prepare_v2(db, queryStatementString, -1, &queryStatement, nil) == SQLITE_OK
            {
                
                if sqlite3_step(queryStatement) == SQLITE_ROW {
                    
                    let id = sqlite3_column_int(queryStatement, 0)
                    
                    let queryResultCol1 = sqlite3_column_text(queryStatement, 1)
                    let name = String.fromCString(UnsafePointer<CChar>(queryResultCol1))!
                    
                    print("Query Result:")
                    print("\(id) | \(name)")
                    
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
            
            if sqlite3_close(db) != SQLITE_OK
            {
                print("error closing database")
            }
            
            db = nil
            
        }
        catch
        {
            print("Invalid Selection.")
        }
       
    }
}

