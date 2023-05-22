package com.mole.qidianchatimg

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 起点接口返回数据类
 * 使用moshi，每个data class都需要添加@JsonClass(generateAdapter = true)
 */
@JsonClass(generateAdapter = true)
data class QiDianResponse(
    val Data: Data,
    val Message: String? = null,
    val Result: String
)
@JsonClass(generateAdapter = true)
data class Data(
    val ABTest: String? = null,
    val AudioCount: String? = null,
    val AuthorInfo: AuthorInfo,
    val BookInfo: BookInfo,
    val CanAuthorForbiddenUserSpeaking: String? = null,
    val CanMarkTop: String? = null,
    val DataList: List<DataX>,
    val TextCount: String? = null,
    val TotalCount: String
)
@JsonClass(generateAdapter = true)
data class AuthorInfo(
    val AuthorId: String? = null,
    val AuthorName: String? = null,
    val UserId: String
)
@JsonClass(generateAdapter = true)
data class BookInfo(
    val BookId: String? = null,
    val BookName: String? = null,
    val BookStatus: String? = null,
    val CategoryName: String? = null,
    val ChapterId: String? = null,
    val ChapterName: String? = null,
    val ExtendChapterFlag: String? = null,
    val HasCopyRight: String? = null,
    val RefferContent: String? = null,
    val WordsCnt: String
)
@JsonClass(generateAdapter = true)
data class DataX(
    val AgreeAmount: String? = null,
    val AudioRoleId: String? = null,
    val AudioTime: String? = null,
    val AudioUrl: String? = null,
    val AuditInfo: AuditInfo,
    val AuthorLike: String? = null,
    val AuthorReviewStatus: String? = null,
    val BigmemeId: String? = null,
    val Category: String? = null,
    val CommentType: String? = null,
    val Content: String? = null,
    val CreateTime: String? = null,
    val EssenceType: String? = null,
    val FaceId: String? = null,
    val Floor: String? = null,
    val FrameId: String? = null,
    val FrameUrl: String? = null,
    val HotAudioStatus: String? = null,
    val Id: String? = null,
    val ImageDetail: String? = null,
    val ImageMeaning: String? = null,
    val InteractionStatus: String? = null,
    val IpLocation: String? = null,
    val IsReplyReview: String? = null,
    val OpposeAmount: String? = null,
    val PageIndex: String? = null,
    val PageSize: String? = null,
    val ParagraphId: String? = null,
    val PreImage: String? = null,
    val RefferCommentId: String? = null,
    val RefferContent: String? = null,
    val RelatedRoleBookId: String? = null,
    val RelatedRoleId: String? = null,
    val RelatedShowTag: String? = null,
    val RelatedShowType: String? = null,
    val RelatedTitleInfoList: List<RelatedTitleInfo>? = null,
    val RelatedUser: String? = null,
    val RelatedUserId: String? = null,
    val ReviewCount: String? = null,
    val ReviewType: String? = null,
    val RoleBookId: String? = null,
    val RoleId: String? = null,
    val RootReviewId: String? = null,
    val ShowTag: String? = null,
    val ShowType: String? = null,
    val StatId: String? = null,
    val TitleInfoList: List<TitleInfo>,
    val TopStatus: String? = null,
    val UgcmemeId: String? = null,
    val UserDisLiked: String? = null,
    val UserHeadIcon: String? = null,
    val UserId: String? = null,
    val UserName: String
)
@JsonClass(generateAdapter = true)
data class AuditInfo(
    val AuditStatus: String? = null,
    val AuditToast: String
)
@JsonClass(generateAdapter = true)
data class RelatedTitleInfo(
    val SerialNumber: String? = null,
    val TitleId: String? = null,
    val TitleImage: String? = null,
    val TitleName: String? = null,
    val TitleShowType: String? = null,
    val TitleSubType: String? = null,
    val TitleType: String
)
@JsonClass(generateAdapter = true)
data class TitleInfo(
    val SerialNumber: String? = null,
    val TitleId: String? = null,
    val TitleImage: String? = null,
    val TitleName: String? = null,
    val TitleShowType: String? = null,
    val TitleSubType: String? = null,
    val TitleType: String
)