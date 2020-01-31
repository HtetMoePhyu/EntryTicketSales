package com.example.entryticketsales.vos

data class BlogModel(
    val etag: String,
    val nextPageToken: String,
    val items: List<Item>,
    val kind: String
)

data class Item(
    val author: Author,
    val blog: Blog,
    val content: String,
    val etag: String,
    val id: String,
    val kind: String,
    val published: String,
    val replies: Replies,
    val selfLink: String,
    val title: String,
    val updated: String,
    val url: String
)

data class Blog(
    val id: String
)

data class Replies(
    val selfLink: String,
    val totalItems: String
)

data class Author(
    val displayName: String,
    val id: String,
    val image: Image,
    val url: String
)

data class Image(
    val url: String
)