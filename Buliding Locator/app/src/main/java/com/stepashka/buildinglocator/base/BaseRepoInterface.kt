package com.stepashka.buildinglocator.base

import com.stepashka.buildinglocator.services.LoginServiceSql
import com.stepashka.buildinglocator.services.ServiceBuilder

interface BaseRepoInterface<T> {

    fun apiFactory(): LoginServiceSql {
        val receiptTrackerFactory by lazy {
            ServiceBuilder.create()
        }
        return receiptTrackerFactory
    }

    fun create(obj: T)

    fun update(obj: T)

    fun delete(obj: T)
}