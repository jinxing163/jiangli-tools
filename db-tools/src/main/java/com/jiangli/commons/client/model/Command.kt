package com.jiangli.commons.client.model

/**
 *
 *
 * @author Jiangli
 * @date 2019/11/20 10:32
 */
open  class Command(open val name:String){

}
data class NameCommand(override val name:String):Command(name){

}

data class SelectOption(val type:String,val name:String)
data class SelectCommand(override val name:String):Command(name){
    var list:MutableList<SelectOption> = mutableListOf()
}