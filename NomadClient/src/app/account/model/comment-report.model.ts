import { Review } from "src/app/accommodation-detail-view/model/accommodationDetails.model";
import { User } from "./user.model";

export interface CommentReport{
    
    reportingAppUser: number;
    reportedComment: number;
    reason: string;
    reportStatus: string;
    id:number;
    reportedCommentDetails?:Review;
    reportingAppUserDetails?:User;

}