package ch.heigvd.iict.daa.labo5

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ClearCacheWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Logique pour vider le cache des images
        // Par exemple, appeler une méthode dans ImageAdapter pour vider le cache
        ImageAdapter.clearCacheStatic()
        return Result.success()
    }
}