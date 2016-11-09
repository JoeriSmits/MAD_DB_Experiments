//
//  SQLite2-Bridging-Header.h
//  SQLite2
//
//  Created by Coen Smid on 06-11-16.
//  Copyright © 2016 Coen Smid. All rights reserved.
//

#ifndef SQLite2_Bridging_Header_h
#define SQLite2_Bridging_Header_h

#import <sqlite3.h>



#define SQLITE_STATIC      ((sqlite3_destructor_type)0)
#define SQLITE_TRANSIENT   ((sqlite3_destructor_type)-1)



#endif /* SQLite2_Bridging_Header_h */
