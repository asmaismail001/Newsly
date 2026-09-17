package com.example.newsly.util

import android.net.Uri
import androidx.media3.common.MimeTypes
import java.util.Locale

object VideoUrlValidator {

    private val KNOWN_NON_VIDEO_EXTENSIONS = setOf(
        "jpg", "jpeg", "png", "gif", "webp", "svg", "bmp", "ico", "tiff",
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
        "zip", "tar", "gz", "rar", "7z",
        "html", "htm", "txt", "json", "xml", "css", "js"
    )

    private val KNOWN_VIDEO_EXTENSIONS = setOf(
        "mp4", "m3u8", "webm", "mkv", "mov", "3gp", "ts", "mpd", "m4v", "avi", "flv"
    )

    /**
     * Checks if the given URL is a valid, potentially playable video stream or media link.
     */
    fun isValidPlayableVideoUrl(rawUrl: String?): Boolean {
        if (rawUrl.isNullOrBlank()) return false
        val trimmed = rawUrl.trim()

        val uri = try {
            Uri.parse(trimmed)
        } catch (e: Exception) {
            return false
        }

        val scheme = uri.scheme?.lowercase(Locale.ROOT)
        if (scheme != "http" && scheme != "https") {
            return false
        }

        val host = uri.host
        if (host.isNullOrBlank()) {
            return false
        }

        val path = uri.path?.lowercase(Locale.ROOT) ?: ""
        val extension = getExtension(path)

        // If it's a known non-video extension (like an image or web page), reject it
        if (extension.isNotEmpty() && KNOWN_NON_VIDEO_EXTENSIONS.contains(extension)) {
            return false
        }

        // If it has a known video extension, accept
        if (extension.isNotEmpty() && KNOWN_VIDEO_EXTENSIONS.contains(extension)) {
            return true
        }

        // Check if query params or path suggest video streaming (e.g., manifest, hls, m3u8, mp4)
        val fullUrlLower = trimmed.lowercase(Locale.ROOT)
        if (fullUrlLower.contains(".m3u8") ||
            fullUrlLower.contains(".mp4") ||
            fullUrlLower.contains("video/") ||
            fullUrlLower.contains("format=m3u8") ||
            fullUrlLower.contains("format=mp4") ||
            fullUrlLower.contains("videoplayback")
        ) {
            return true
        }

        // If there's no extension or ambiguous extension, and it's a valid HTTP/HTTPS url that wasn't blocked as an image
        return extension.isEmpty()
    }

    /**
     * Determines MIME type hint if available from the URL format for Media3.
     */
    fun getMimeTypeForUrl(url: String?): String? {
        if (url.isNullOrBlank()) return null
        val lower = url.lowercase(Locale.ROOT)

        return when {
            lower.contains(".m3u8") || lower.contains("hls") || lower.contains("format=m3u8") -> {
                MimeTypes.APPLICATION_M3U8
            }
            lower.contains(".mpd") || lower.contains("dash") -> {
                MimeTypes.APPLICATION_MPD
            }
            lower.contains(".webm") -> {
                MimeTypes.VIDEO_WEBM
            }
            lower.contains(".mp4") || lower.contains(".m4v") -> {
                MimeTypes.VIDEO_MP4
            }
            else -> null
        }
    }

    /**
     * Cleans and sanitizes the video URL.
     */
    fun sanitizeVideoUrl(rawUrl: String?): String? {
        if (rawUrl.isNullOrBlank()) return null
        var trimmed = rawUrl.trim()
        // If HTTP, upgrade to HTTPS if standard host
        if (trimmed.startsWith("http://", ignoreCase = true)) {
            trimmed = "https://" + trimmed.substring(7)
        }
        return trimmed
    }

    private fun getExtension(path: String): String {
        val lastDotIndex = path.lastIndexOf('.')
        if (lastDotIndex == -1 || lastDotIndex == path.length - 1) return ""
        val ext = path.substring(lastDotIndex + 1)
        val slashIndex = ext.indexOf('/')
        return if (slashIndex != -1) ext.substring(0, slashIndex) else ext
    }
}
