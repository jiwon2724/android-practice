/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.hilt.data

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data manager class that handles data manipulation between the database and the UI.
 */

/**
 * @Singleton 어노테이션을 사용하여 인스턴스의 범위를 컨테이너로 지정할 수 있다.
 *  -> 인스턴스 범위를 애플리케이션 컨테이너로 지정하는 어노테이션이다.
 *  다른 유형의 종속 항목으로 사용되는지, 삽입된 필드여야 하는지와 관계없이 애플리케이션 컨테이너에서 항상 같은 인스턴스를 제공한다.
 *
 *
 * **/
@Singleton
class LoggerLocalDataSource @Inject constructor(private val logDao: LogDao) {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    fun addLog(msg: String) {
        executorService.execute {
            logDao.insertAll(
                Log(
                    msg,
                    System.currentTimeMillis()
                )
            )
        }
    }

    fun getAllLogs(callback: (List<Log>) -> Unit) {
        executorService.execute {
            val logs = logDao.getAll()
            mainThreadHandler.post { callback(logs) }
        }
    }

    fun removeLogs() {
        executorService.execute {
            logDao.nukeTable()
        }
    }
}
