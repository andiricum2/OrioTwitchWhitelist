package com.orioland

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private val whitelist = mutableListOf<UserData>()
class Whitelist {
   private val whitelistFile = File("plugins/TwitchWhitelist/whitelist.json")
   init {
      if (!whitelistFile.exists()) {
         whitelistFile.createNewFile()
         whitelistFile.writeText(GsonBuilder().setPrettyPrinting().create().toJson(whitelist))
      }
   }
   fun load() = whitelist.addAll(GsonBuilder().setPrettyPrinting().create().fromJson(whitelistFile.readText(), object : TypeToken<List<UserData>>() {}.type))
   fun whitelist(userData: UserData): Boolean {
      if (isWhitelisted(userData.uuid)) return false
      whitelist.add(userData)
      whitelistFile.writeText(GsonBuilder().setPrettyPrinting().create().toJson(whitelist))
      return true
   }
   fun unwhitelist(uuid: String): Boolean {
      if (!isWhitelisted(uuid)) return false
      whitelist.removeIf { it.uuid == uuid }
      whitelistFile.writeText(GsonBuilder().setPrettyPrinting().create().toJson(whitelist))
      return true
   }
   fun isWhitelisted(uuid: String): Boolean = whitelist.any { it.uuid == uuid.replace("-", "") }
   fun playerNames(): List<String> = whitelist.map { it.name }
   fun usedWhitelist(twitchUserID: String): Boolean {
      if (twitchUserID == channelID) return false
      if (whitelist.count { it.twitchUserID == twitchUserID } >= ticketPerUser) return true
      return false
   }
   fun userDataByName(name: String): UserData? = whitelist.firstOrNull { it.name == name }
}
data class UserData(val name: String, val uuid: String, val twitchUserID: String)

fun String.getUserDataFromName(): Response? = getRequest("https://api.mojang.com/users/profiles/minecraft/$this")
data class Response(val name: String, val id: String)
fun getRequest(url: String): Response? {
   val client = HttpClient.newBuilder().build()
   val request = HttpRequest.newBuilder()
      .uri(URI.create(url))
      .build()
   return Gson().fromJson(client.send(request, HttpResponse.BodyHandlers.ofString()).body(), Response::class.java)
}