package br.pucpr.appdev.schuhspizzaria.dao

interface GenericDao<T> {
    fun getAll() : MutableList<T>
    fun getById(id: Long) : T
    fun remove(obj : T) : Long
    fun edit(obj : T) : Long
    fun add(obj : T) : Long
}