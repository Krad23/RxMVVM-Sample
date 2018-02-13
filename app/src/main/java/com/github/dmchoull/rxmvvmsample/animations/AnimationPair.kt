package com.github.dmchoull.rxmvvmsample.animations

import android.app.Activity
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.transition.TransitionManager

class AnimationPair(
    private val activity: Activity,
    private val root: ConstraintLayout,
    private val targetLayout: Int
) {
    private val firstConstraint = ConstraintSet().apply {
        clone(root)
    }
    private val secondConstraint = ConstraintSet().apply {
        clone(activity, targetLayout)
    }

    fun animate(forward: Boolean) {
        TransitionManager.beginDelayedTransition(root)
        val target = if (forward) secondConstraint else firstConstraint
        target.applyTo(root)
    }

    internal class Factory(private val activity: Activity) {
        fun create(
            root: ConstraintLayout,
            targetLayout: Int
        ) = AnimationPair(activity, root, targetLayout)
    }
}