package com.sdm.githubusersearch.animation

import android.animation.AnimatorSet
import android.app.Application
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewParent
import android.widget.ImageView
import com.sdm.githubusersearch.argumentCaptor
import com.sdm.githubusersearch.mock
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*

class ZoomThumbTest {

    private val resources = mock<Resources>()
    private val application = mock<Application>()
    private val thumbView = mock<View>()
    private val expandedView = mock<ImageView>()
    private val parentView = Mockito.mock(
        View::class.java, withSettings().extraInterfaces(
            ViewParent::class.java
        )
    ) as ViewParent
    private val drawable = mock<Drawable>()
    private val animationFactory = mock<AnimationFactory>()
    private val animatorSet = mock<AnimatorSet>()
    private val expandedClickListener = argumentCaptor<View.OnClickListener>()
    private val zoomThumb: ZoomThumb

    init {
        `when`(expandedView.parent).thenReturn(parentView)
        `when`(application.resources).thenReturn(resources)
        `when`(animationFactory.createAnimatorSet()).thenReturn(animatorSet)
        `when`(animatorSet.play(anyObject())).thenReturn(mock())
        `when`(expandedView.setOnClickListener(expandedClickListener.capture())).then {}
        zoomThumb = ZoomThumb(application, animationFactory)
    }

    @Test
    fun `start zoom in animation`() {
        zoomThumb.zoomImageFromThumb(thumbView, expandedView, drawable)

        verify(animatorSet).start()
    }

    @Test
    fun `start zoom out animation`() {
        zoomThumb.zoomImageFromThumb(thumbView, expandedView, drawable)
        expandedClickListener.value.onClick(null)

        val inOrder = inOrder(animatorSet)
        inOrder.verify(animatorSet).start()
        inOrder.verify(animatorSet).cancel()
        inOrder.verify(animatorSet).start()
    }

    private fun <T> anyObject(): T {
        return Mockito.anyObject<T>()
    }
}
