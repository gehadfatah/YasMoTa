package com.goda.movieapp.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerAppCompatActivity

/*
fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle? = null) {
    val destinationId = currentDestination?.getAction(resId)?.destinationId.orEmpty()
    currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        if (destinationId != EMPTY_INT) {
            currentNode?.findNode(destinationId)?.let { navigate(resId, args) }
        }
    }}*/

@SuppressLint("MissingPermission")
 fun Activity.isNetworkConnected(): Boolean {
    val connMgr = this.getSystemService(DaggerAppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connMgr.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}
fun Context.showLongToast(msgId: Int) {
    Toast.makeText(this, getString(msgId), Toast.LENGTH_SHORT).show()
}
fun Context.showLongToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}
fun Context.showShortToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
fun Fragment.goldEventChangedListener(key: String, listener: (String) -> Unit) {

    val currentStateEntry = findNavController().currentBackStackEntry
    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME
            && currentStateEntry?.savedStateHandle?.contains(key) == true
        ) {
            val result = currentStateEntry.savedStateHandle.get<String>(key)
            result?.let(listener)
            removeEventStateValue(key)
        }
    }
    currentStateEntry?.lifecycle?.addObserver(observer)

    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            currentStateEntry?.lifecycle?.removeObserver(observer)
        }
    })
}
fun Fragment.removeEventStateValue(key: String) {
    findNavController().currentBackStackEntry?.savedStateHandle?.remove<String>(
        key
    )
}
fun Fragment.setEventStateValue(key: String, value: String) {
    if (value.isNotEmpty()) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            key,
            value
        )
    }
}