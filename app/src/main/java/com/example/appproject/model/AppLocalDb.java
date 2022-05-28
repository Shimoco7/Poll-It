package com.example.appproject.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.appproject.MyApplication;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.detail.DetailDao;
import com.example.appproject.model.poll.Answer;
import com.example.appproject.model.poll.AnswerDao;
import com.example.appproject.model.poll.Poll;
import com.example.appproject.model.poll.PollDao;
import com.example.appproject.model.poll.PollQuestion;
import com.example.appproject.model.poll.PollQuestionDao;
import com.example.appproject.model.question.Question;
import com.example.appproject.model.question.QuestionDao;
import com.example.appproject.model.reward.Order;
import com.example.appproject.model.reward.Reward;
import com.example.appproject.model.reward.RewardDao;
import com.example.appproject.model.user.User;
import com.example.appproject.model.user.UserDao;
import com.example.appproject.model.user.UserPollCrossRef;


@Database(entities ={User.class, Detail.class, Question.class, PollQuestion.class, Poll.class, Answer.class, UserPollCrossRef.class, Reward.class, Order.class},
        version = 43 ,exportSchema = false)
@TypeConverters({Converters.class})
abstract class AppLocalDbRepository extends RoomDatabase{
    public abstract UserDao userDao();
    public abstract DetailDao detailDao();
    public abstract PollDao pollDao();
    public abstract PollQuestionDao pollQuestionDao();
    public abstract QuestionDao questionDao();
    public abstract AnswerDao answerDao();
    public abstract RewardDao rewardDao();
}

public class AppLocalDb {
    public static AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),AppLocalDbRepository.class,"PollItDb.db")
                    .fallbackToDestructiveMigration()
                    .build();
}


