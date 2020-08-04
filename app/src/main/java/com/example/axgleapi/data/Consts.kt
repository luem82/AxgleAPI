package com.example.axgleapi.data

import com.example.axgleapi.data.video.Video

object Consts {

    const val INTENT_NAME = "ch_title"
    const val INTENT_CHID = "ch_id"
    const val TITLE_NULL = "null_video_title"
    const val LAST_VIEWED = "bw"
    const val LATEST = "mr"
    const val MOST_VIEWED = "mv"
    const val TOP_RATED = "tr"
    const val MOST_FAVOURED = "tf"
    const val LONGEST = "lg"
    const val LIMIT_50 = 50
    const val LIMIT_250 = 250

    val VIDEO_NULL = Video(
        false, false, 0.0, 0L, 0L, 0L, 0L,
        0.0, "", "", "", TITLE_NULL, false
    )

}