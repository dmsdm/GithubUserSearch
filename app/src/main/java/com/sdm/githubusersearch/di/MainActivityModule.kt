package com.sdm.githubusersearch.di

import com.sdm.githubusersearch.MainActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [])
    abstract fun contributeMainActivity(): MainActivity
}
