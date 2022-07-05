package com.jianzhi.taskhandler
import kotlin.collections.ArrayList

class TaskHandler  {
companion object{
    var tool: TaskHandler? = null
    val newInstance: callback
    get(){
        if(tool ==null){
            tool = TaskHandler()
        }
        return  tool!!.callback
    }

}
    var callback=object : callback {
        override fun clock(): JzClock {
            return JzClock()
        }

        override fun runTaskOnce(tag: String, callback: runner, cantrun:runner) {
            if(runtag.contains(tag)){
                cantrun.run()
                return}else{
                runtag.add(tag)
                callback.run()
                runtag.remove(tag)
            }
        }
        override fun runTaskTimer(tag: String,time:Double, callback: runner,cantrun:runner) {
            for(i in runnerTimer){
                if(i.tag==tag){
                    if(i.timer.stop()<time){
                        cantrun.run()
                        return}
                    }
            }
            val seq= (runnerTimer).filter { it.tag!=tag }
            runnerTimer= ArrayList(seq)
            val timertask= TimerTask()
            timertask.tag=tag
            timertask.timer= JzClock()
            runnerTimer.add(timertask)
            callback.run()
        }

        override fun runTaskMultiple(tag: String,count:Int, callback: runner,cantrun:runner) {
            var havecount=0
            for(i in runtag){
                if(i==tag){havecount+=1}
            }
            if(havecount==count){
                cantrun.run()
                return}
                runtag.add(tag)
                callback.run()
                runtag.remove(tag)
            }
        }
    var runtag=ArrayList<String>()
    var runnerTimer=ArrayList<TimerTask>()
    }


