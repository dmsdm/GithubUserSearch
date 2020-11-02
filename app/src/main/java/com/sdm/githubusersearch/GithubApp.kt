package com.sdm.githubusersearch

import android.app.Activity
import android.app.Application
import com.sdm.githubusersearch.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class GithubApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }
    override fun activityInjector() = dispatchingAndroidInjector
}
