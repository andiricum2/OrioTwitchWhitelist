package com.orioland

import org.bukkit.ChatColor

val prefix = "${ChatColor.GOLD}Whitelist ${ChatColor.WHITE}•${ChatColor.GRAY}"
var enabled: Boolean = true
var chatToken: String = ""
var channelID: String = ""
var ticketPerUser: Int = 1
var serverName: String = ""
var channelRewardName: String = ""
var sendMessage: Boolean = true
var successMessage: String = ""
var playerNotFoundMessage: String = ""
var alreadyWhitelistedMessage: String = ""
var alreadyWhitelistedOnePlayerMessage: String = ""
var notWhitelistedText: String = ""