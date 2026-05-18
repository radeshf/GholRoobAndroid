package ir.radesh.basemodule.interfaces

interface DataInterface<T>{
        fun onDataReceived(response: T)
        fun onError(e: Throwable)
    }
