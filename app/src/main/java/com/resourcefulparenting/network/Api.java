package com.resourcefulparenting.network;

import com.resourcefulparenting.models.ActivityComListingResponse;
import com.resourcefulparenting.models.ActivityDetailsResponse;
import com.resourcefulparenting.models.AcyivitySendComResponse;
import com.resourcefulparenting.models.AlarmResponse;
import com.resourcefulparenting.models.ChildDetailsResponse;
import com.resourcefulparenting.models.ImageDeleteResponse;
import com.resourcefulparenting.models.Input.ActivityComplistCheck;
import com.resourcefulparenting.models.Input.ActivityDetailsCheck;
import com.resourcefulparenting.models.Input.ActivityListCheck;
import com.resourcefulparenting.models.Input.ActivitySendCheck;
import com.resourcefulparenting.models.Input.AlarmCheck;
import com.resourcefulparenting.models.Input.ChildeImageCheck;
import com.resourcefulparenting.models.Input.DeleteChildCheck;
import com.resourcefulparenting.models.Input.ImageDeleteCheck;
import com.resourcefulparenting.models.Input.MilestoneQuestionCheck;
import com.resourcefulparenting.models.Input.NotificationSend;
import com.resourcefulparenting.models.Input.TodaysactivityCheck;
import com.resourcefulparenting.models.Input.UpdatechildCheck;
import com.resourcefulparenting.models.Input.VideoCheck;
import com.resourcefulparenting.models.Input.MilestoneQuestionSend;
import com.resourcefulparenting.models.MilestoneQuestionsResponse;
import com.resourcefulparenting.models.MilestoneQuestionsSendResponse;
import com.resourcefulparenting.models.NotificationResponse;
import com.resourcefulparenting.models.ProfileImageResponse;
import com.resourcefulparenting.models.QueriesResponse;
import com.resourcefulparenting.models.AddChild.AddChildCheck;
import com.resourcefulparenting.models.AddChild.AddChildResponse;
import com.resourcefulparenting.models.ChangePasswordCheck;
import com.resourcefulparenting.models.ForgotPasswordCheck;
import com.resourcefulparenting.models.CommonResponse;
import com.resourcefulparenting.models.Login.LoginCheck;
import com.resourcefulparenting.models.Login.LoginResponse;
import com.resourcefulparenting.models.LogoutCheck;
import com.resourcefulparenting.models.Register.AcivityChildUpdate;
import com.resourcefulparenting.models.Register.ActivityListing;
import com.resourcefulparenting.models.Register.ChildDeleteResponse;
import com.resourcefulparenting.models.Register.RegisterCheck;
import com.resourcefulparenting.models.Register.RegisterResponse;
import com.resourcefulparenting.models.SetParentNameCheck;
import com.resourcefulparenting.models.TodayAcyivityResponse;
import com.resourcefulparenting.models.VideoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Api {

    @POST("registration")
    Call<RegisterResponse> registerParent(@Body RegisterCheck registerCheck);

    @POST("login")
    Call<LoginResponse> Login(@Body LoginCheck loginCheck);

    @POST("forgetpassword")
    Call<CommonResponse> ForgotPassword(@Body ForgotPasswordCheck forgotPasswordCheck);

    @POST("logout")
    Call<CommonResponse> Logout(@Body LogoutCheck logoutCheck);

    @POST("changeparentname")
    Call<CommonResponse> ChangeParentName(@Body SetParentNameCheck setParentNameCheck);

    @POST("changepassword")
    Call<CommonResponse> ChangePassword(@Body ChangePasswordCheck changePasswordCheck);

    @POST("addchild")
    Call<AddChildResponse> AddChild(@Body AddChildCheck addChildCheck);

    @GET("allqueries")
    Call<QueriesResponse> getQueries();

    @POST("todaysactivity")
    Call<TodayAcyivityResponse> getTodayActivity(@Body TodaysactivityCheck addChildCheck);

    @POST("activitycompleted")
    Call<AcyivitySendComResponse> seAcyivitySendComResponseCall(@Body ActivitySendCheck addChildCheck);

    @POST("activitycompletedlist")
    Call<ActivityComListingResponse> ACYIVITY_COM_LISTING_RESPONSE_CALL(@Body ActivityComplistCheck addChildCheck);

    @POST("activitydetails")
    Call<ActivityDetailsResponse> activityDetailsResponseCall(@Body ActivityDetailsCheck activityDetailsCheck);


    @POST("activitylist")
    Call<ActivityListing> ACTIVITY_LISTING_CALL(@Body ActivityListCheck addChildCheck);


    @POST("updatechildinfo")
    Call<AcivityChildUpdate> updateResponseCall(@Body UpdatechildCheck addChildCheck);


    @POST("deletechild")
    Call<ChildDeleteResponse> deleteResponseCall(@Body DeleteChildCheck addChildCheck);

    @GET
    Call<ChildDetailsResponse> childDetailsResponseCall(@Url String url);

    @POST("childprofile")
    Call<ProfileImageResponse> profileImageResponse(@Body ChildeImageCheck addChildCheck);

    @POST("childactivityimg")
    Call<ProfileImageResponse> ImageResponse(@Body ChildeImageCheck addChildCheck);

    @POST("childactivityyoutubelink")
    Call<VideoResponse> videoResponse(@Body VideoCheck addChildCheck);

    @POST("setalarm")
    Call<AlarmResponse> alarmResponse(@Body AlarmCheck addChildCheck);

    @POST("deletechildactivityimg")
    Call<ImageDeleteResponse> imageDeleteResponse(@Body ImageDeleteCheck addChildCheck);

    @POST("milestonequestions")
    Call<MilestoneQuestionsResponse> milestoneQuestionsResponse(@Body MilestoneQuestionCheck addChildCheck);


    @POST("submitmilestonequestions")
    Call<MilestoneQuestionsSendResponse> milestoneQuestionsSend(@Body MilestoneQuestionSend addChildCheck);

    @POST("registernotificationtoken")
    Call<NotificationResponse> notificationResponse(@Body NotificationSend addChildCheck);
}