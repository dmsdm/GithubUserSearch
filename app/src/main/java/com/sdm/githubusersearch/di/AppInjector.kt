package com.sdm.githubusersearch.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.sdm.githubusersearch.GithubApp
import dagger.android.AndroidInjection
import dagger.android.support.HasSupportFragmentInjector

object AppInjector {
    fun init(githubApp: GithubApp) {
        DaggerAppComponent.builder().application(githubApp)
            .build().inject(githubApp)
        githubApp
            .registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    if (activity is HasSupportFragmentInjector) {
                        AndroidInjection.inject(activity)
                    }
                }

                override fun onActivityStarted(activity: Activity) {

                }

                override fun onActivityResumed(activity: Activity) {

                }

                override fun onActivityPaused(activity: Activity) {

                }

                override fun onActivityStopped(activity: Activity) {

                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

                }

                override fun onActivityDestroyed(activity: Activity) {

                }
            })
    }
}
