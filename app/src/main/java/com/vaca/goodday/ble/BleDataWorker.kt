package com.vaca.goodday.ble


import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Handler
import android.util.Log
import com.vaca.goodday.MainApplication
import com.viatom.checkme.ble.manager.BleDataManager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import no.nordicsemi.android.ble.callback.FailCallback
import no.nordicsemi.android.ble.data.Data
import no.nordicsemi.android.ble.observer.ConnectionObserver
import org.greenrobot.eventbus.EventBus
import java.lang.System.currentTimeMillis
import java.lang.Thread.sleep
import java.util.*

class BleDataWorker {

    data class Pc100Data(val event:Pc100Event,val time:Long,val sys:Int=0,val dia:Int=0,val pr:Int=0,val o2:Int=0,val o2pr:Int=0)

    enum class Pc100Event{
        GoToResult,O2Data,BpData,TakeOffO2,BpPresent
    }


    private var pool: ByteArray? = null
    private val fileChannel = Channel<Int>(Channel.CONFLATED)
    private val connectChannel = Channel<String>(Channel.CONFLATED)
    var myBleDataManager: BleDataManager? = null
    private val dataScope = CoroutineScope(Dispatchers.IO)
    private val mutex = Mutex()


    private var cmdState = 0;
    var pkgTotal = 0;
    var currentPkg = 0;
    var fileData: ByteArray? = null
    var currentFileName = ""
    var result = 1;
    var currentFileSize = 0
    var lastMill = 0L
    var startMill = 0L


    var lastMill2 = 0L
    var endSeven = false


    companion object {
        val pc100Stack= ArrayList<Pc100Data>()
        val Pc100Handler= Handler()
        var startPcMeasureTime=0L
    }







    var o2 = 0
    var prx = 0

    var sys = 0;
    var dia = 0
    var pr = 0
    private val comeData = object : BleDataManager.OnNotifyListener {
        override fun onNotify(device: BluetoothDevice?, data: Data?) {

        }
    }


    fun sendCmd(bs: ByteArray) {
        myBleDataManager?.sendCmd(bs)
    }

    private val connectState = object : ConnectionObserver {
        override fun onDeviceConnecting(device: BluetoothDevice) {

        }

        override fun onDeviceConnected(device: BluetoothDevice) {

        }

        override fun onDeviceFailedToConnect(device: BluetoothDevice, reason: Int) {

        }

        override fun onDeviceReady(device: BluetoothDevice) {

        }

        override fun onDeviceDisconnecting(device: BluetoothDevice) {

        }

        override fun onDeviceDisconnected(device: BluetoothDevice, reason: Int) {

        }

    }


    fun initWorker(context: Context, bluetoothDevice: BluetoothDevice?) {
        try {
            myBleDataManager?.disconnect()?.enqueue()
            sleep(200)
        } catch (ep: Exception) {

        }

        bluetoothDevice?.let {
            myBleDataManager?.connect(it)
                ?.useAutoConnect(false)
                ?.timeout(10000)
                ?.retry(15, 100)
                ?.done {
                    Log.i("BLE", "连接成功了.>>.....>>>>")


                }?.fail(object : FailCallback {
                    override fun onRequestFailed(device: BluetoothDevice, status: Int) {

                    }

                })
                ?.enqueue()
        }
    }

    suspend fun waitConnect() {
        connectChannel.receive()
    }

    init {
        myBleDataManager = BleDataManager(MainApplication.application)
        myBleDataManager?.setNotifyListener(comeData)
        myBleDataManager?.setConnectionObserver(connectState)
    }


    fun disconnect() {
        myBleDataManager?.disconnect()?.enqueue()
    }
}