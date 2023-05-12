package com.mole.qidianchatimg

import com.squareup.moshi.JsonClass

/**
 * 起点接口返回数据类
 * 使用moshi，每个data class都需要添加@JsonClass(generateAdapter = true)
 */
@JsonClass(generateAdapter = true)
data class QiDianResponse(
    val Data: Data,
    val Message: String,
    val Result: String
)
@JsonClass(generateAdapter = true)
data class Data(
    val ABTest: String,
    val AudioCount: String,
    val AuthorInfo: AuthorInfo,
    val BookInfo: BookInfo,
    val CanAuthorForbiddenUserSpeaking: String,
    val CanMarkTop: String,
    val DataList: List<DataX>,
    val TextCount: String,
    val TotalCount: String
)
@JsonClass(generateAdapter = true)
data class AuthorInfo(
    val AuthorId: String,
    val AuthorName: String,
    val UserId: String
)
@JsonClass(generateAdapter = true)
data class BookInfo(
    val BookId: String,
    val BookName: String,
    val BookStatus: String,
    val CategoryName: String,
    val ChapterId: String,
    val ChapterName: String,
    val ExtendChapterFlag: String,
    val HasCopyRight: String,
    val RefferContent: String,
    val WordsCnt: String
)
@JsonClass(generateAdapter = true)
data class DataX(
    val AgreeAmount: String,
    val AudioRoleId: String,
    val AudioTime: String,
    val AudioUrl: String,
    val AuditInfo: AuditInfo,
    val AuthorLike: String,
    val AuthorReviewStatus: String,
    val BigmemeId: String,
    val Category: String,
    val CommentType: String,
    val Content: String,
    val CreateTime: String,
    val EssenceType: String,
    val FaceId: String,
    val Floor: String,
    val FrameId: String,
    val FrameUrl: String,
    val HotAudioStatus: String,
    val Id: String,
    val ImageDetail: String,
    val ImageMeaning: String,
    val InteractionStatus: String,
    val IpLocation: String,
    val IsReplyReview: String,
    val OpposeAmount: String,
    val PageIndex: String,
    val PageSize: String,
    val ParagraphId: String,
    val PreImage: String,
    val RefferCommentId: String,
    val RefferContent: String,
    val RelatedRoleBookId: String,
    val RelatedRoleId: String,
    val RelatedShowTag: String,
    val RelatedShowType: String,
    val RelatedTitleInfoList: List<RelatedTitleInfo>,
    val RelatedUser: String,
    val RelatedUserId: String,
    val ReviewCount: String,
    val ReviewType: String,
    val RoleBookId: String,
    val RoleId: String,
    val RootReviewId: String,
    val ShowTag: String,
    val ShowType: String,
    val StatId: String,
    val TitleInfoList: List<TitleInfo>,
    val TopStatus: String,
    val UgcmemeId: String,
    val UserDisLiked: String,
    val UserHeadIcon: String,
    val UserId: String,
    val UserName: String
)
@JsonClass(generateAdapter = true)
data class AuditInfo(
    val AuditStatus: String,
    val AuditToast: String
)
@JsonClass(generateAdapter = true)
data class RelatedTitleInfo(
    val SerialNumber: String,
    val TitleId: String,
    val TitleImage: String,
    val TitleName: String,
    val TitleShowType: String,
    val TitleSubType: String,
    val TitleType: String
)
@JsonClass(generateAdapter = true)
data class TitleInfo(
    val SerialNumber: String,
    val TitleId: String,
    val TitleImage: String,
    val TitleName: String,
    val TitleShowType: String,
    val TitleSubType: String,
    val TitleType: String
)