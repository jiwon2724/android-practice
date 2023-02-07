package com.example.android.hilt.di

import com.example.android.hilt.data.AppDatabase
import com.example.android.hilt.data.LogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager


// Hilt에서 삽입할 수 있는 각 Android 클래스에는 연결된 Hilt 구성요소가 있습니다.
// Application 컨테이너는 ApplicationComponent와 연결되며 Fragment 컨테이너는 FragmentComponent와 연결된다.

/**
 *
 * 모듈을 사용하여 Hilt에 다양한 유형의 인스턴스 제공 방법을 알려준다.
 * 인터페이스나 프로젝트에 포함되지 않은 클래스와 같이 생성자가 삽입될 수 없는 유형의 결합을 Hilt 모듈에 제공한다.
 * Hilt 모듈은 @Module과 @InstallIn 주석이 달린 클래스이다. @Module은 Hilt에 모듈임을 알려주고 @InstallIn은
 * 어느 컨테이너에 Hilt 구성요소를 지정하여 결압을 사용할 수 있는지 Hilt에 알려준다.
 *
 * **/

@InstallIn(ApplicationComponentManager::class)
@Module
object DatabaseModule {
    /**
     * Hilt 모듈에 있는 함수에 @Provides 주석을 달아 Hilt에 생성자가 삽일될 수 없는 유형의 제공 방법을 알려 줄 수 있습니다.
     * @Provides 주석이 있는 함수 본문은 Hilt에서 이 유형의 인스턴스를 제공해야 할 때마다 실행된다.
     * @Porivdes 주석이 있는 함수의 반환 유형은 Hilt에 결합 유형 또는 유형의 인스턴스 제공 방법을 알려줌. 매개변수는 유형의 종속 항목이다
     *
     *
     * **/
    @Provides
    fun provideLogDao(database: AppDatabase) : LogDao {
        return database.logDao()
    }
}