package com.cloudnine.roomdemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subscriber::class], version = 1,exportSchema = false)
abstract class SubscriberDataBase : RoomDatabase() {

    abstract val SubscriberDAO: SubscriberDAO

   companion object{
        @Volatile
        private var instance: SubscriberDataBase? = null
        fun getInstance(context:Context):SubscriberDataBase
        {
            synchronized(this)
            {
                var instance = instance
                if(instance==null)
                {
                    instance= Room.databaseBuilder(context.applicationContext,SubscriberDataBase::class.java,"subscriber_data_database").build()

                }
                return instance
            }

        }
    }
 /* companion object{
      @Volatile
      private var INSTANCE : SubscriberDataBase? = null
      fun getInstance(context: Context):SubscriberDataBase{
          synchronized(this){
              var instance = INSTANCE
              if(instance==null){
                  instance = Room.databaseBuilder(
                      context.applicationContext,
                      SubscriberDataBase::class.java,
                      "subscriber_data_database"
                  ).build()
              }
              return instance
          }
      }

  }*/
}