package io.github.theriverelder.data
//
//import org.ktorm.schema.Table
//import org.ktorm.schema.int
//import org.ktorm.schema.long
//import org.ktorm.schema.varchar
//
//object Entities : Table<Nothing>("entities") {
//    val uid = long("uid").primaryKey()
//    val name = varchar("uid")
//    val gameUId = varchar("game_uid")
//    val ownerUid = varchar("owner_uid")
//}
//
//object Properties : Table<Nothing>("properties") {
//    val uid = long("uid").primaryKey()
//    val gameId = long("game_uid")
//    val entityId = long("entity_uid")
//    val value = int("value")
//    val limit = int("limit")
//}
//
//object Games : Table<Nothing>("games") {
//    val uid = long("uid").primaryKey()
//    val name = varchar("name")
//}
//
//object Groups : Table<Nothing>("groups") {
//    val uid = long("uid").primaryKey()
//    val currentGameUid = long("current_game_uid")
//    val adminUid = long("admin_uid")
//}
//
//object Players : Table<Nothing>("players") {
//    val uid = long("uid").primaryKey()
//    val currentGameUid = long("current_game_uid")
//    val adminUid = long("admin_uid")
//}