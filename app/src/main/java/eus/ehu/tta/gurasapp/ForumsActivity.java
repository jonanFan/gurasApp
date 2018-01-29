package eus.ehu.tta.gurasapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import eus.ehu.tta.gurasapp.model.Forum;
import eus.ehu.tta.gurasapp.model.Forums;
import eus.ehu.tta.gurasapp.presentation.LocalStorage;

public class ForumsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);

        Forums forums = LocalStorage.getForums(this);
        if (forums != null) {
            createScrollView(forums);
        }
    }

    private void createScrollView(Forums forums) {
        LinearLayout scrollView = findViewById(R.id.scrollForums);
        scrollView.removeAllViews();

        List<Forum> forumsList = forums.getForums();

        LayoutInflater inflater = LayoutInflater.from(ForumsActivity.this);


        for (int i = 0; i < forums.getTotal(); i++) {

            String title = forumsList.get(i).getTitle();
            String question = forumsList.get(i).getQuestion();
            String answer = forumsList.get(i).getAnswer();
            String teacher = forumsList.get(i).getTeacher();

            Log.d(GURASAPP_ACTIVITY_TAG, "Forum: La question es " + question);

            View view = inflater.inflate(R.layout.frame_forum, null, false);
            TextView textView = view.findViewById(R.id.forumTitle);
            textView.setText(title);

            if (answer != null && !answer.isEmpty()) {
                textView = view.findViewById(R.id.forumAnswerContent);
                textView.setText(answer);
                textView.setVisibility(View.VISIBLE);
                view.findViewById(R.id.forumAnswerTitle).setVisibility(View.VISIBLE);
            }

            if (teacher != null && !teacher.isEmpty()) {
                textView = view.findViewById(R.id.forumTeacherContent);
                textView.setText(teacher);
                textView.setVisibility(View.VISIBLE);
                view.findViewById(R.id.forumTeacherTitle).setVisibility(View.VISIBLE);
            }

            scrollView.addView(view);
        }
    }

    @Override
    public void changeLangToEs(View view) {
        super.changeLangToEs(view);
    }

    @Override
    public void changeLangToEu(View view) {
        super.changeLangToEu(view);
    }
}
