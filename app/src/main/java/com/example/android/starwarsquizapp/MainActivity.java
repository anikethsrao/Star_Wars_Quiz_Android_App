package com.example.android.starwarsquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This app quizzes the user on how well they know their Star Wars Lore
 * updated 10/02/2021
 * Aniketh Rao
 */

public class MainActivity extends AppCompatActivity {
    int numberOfCorrect = 0;
    /**
     * Single choice MC questions
     */
    final String[] singleChoiceQuestions = {
            "Who defeated Darth Vader?",
            "Who is Han Solo's best friend?",
            "What is Chancellor Palpatine's real identity?",
            "What is \"Baby Yoda\"'s real name?",
    };
    /**
     * Answer options for each of the single choice MC questions
     * The first value of the array is the answer to the question
     */
    final String[][] singleChoiceQuestionOptions = {
            {"Luke Skywalker", "Anikin Skywalker", "Princess Leia", "Lando Calrissian"},
            {"Chewbacca", "R2-D2", "C-3PO", "Mace Windu"},
            {"Darth Sidious", "Darth Maul", "Darth Vader", "Anikin Skywalker"},
            {"Grogu", "Little Boy", "The Child", "Jeffry"},
    };
    /**
     * Multiple choice questions
     * Second question does not have a correct answer
     */
    final String[] multipleChoiceQuestion = {
            "What colours do Jedi knights use?",
            "Which colours would you use?"
    };
    /**
     * The selection choices for the multiple choice questions
     */
    final String[][] multipleChoiceQuestionOptions = {
            {"Blue", "Green", "Purple", "Red"},
            {"Blue", "Green", "Purple", "Red"}
    };
    /**
     * Text Input questions
     * Second array value is the correct answer to the question
     */
    final String[][] textInputQuestions = {
            {"What planet did Yoda die?", "Dagobah"}
    };
    /**
     * Calculations to easily setup the size of the correct answers boolean
     */
    final int numberOfSingleAnswerQuestions = singleChoiceQuestions.length;
    final int numberOfMultipleChoiceQuestions = multipleChoiceQuestion.length - 1;
    final int numberOfTextInputQuestions = textInputQuestions.length;
    /**
     * Boolean keeps track of the questions the user selects correctly or incorrectly for results display
     */
    boolean[] correctAnswers = new boolean[numberOfSingleAnswerQuestions + numberOfMultipleChoiceQuestions + numberOfTextInputQuestions];
    SingleChoiceQuestionObject[] singleChoiceQuestionObject = {
            new SingleChoiceQuestionObject(),
            new SingleChoiceQuestionObject(),
            new SingleChoiceQuestionObject(),
            new SingleChoiceQuestionObject()
    };
    String summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Inflate all views
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView[] questionTextViews = {
                findViewById(R.id.question_1),
                findViewById(R.id.question_2),
                findViewById(R.id.question_3),
                findViewById(R.id.question_4)
        };
        RadioGroup[] optionRadioViews = {
                findViewById(R.id.question_1_options),
                findViewById(R.id.question_2_options),
                findViewById(R.id.question_3_options),
                findViewById(R.id.question_4_options),
        };
        for (int i = 0; i < numberOfSingleAnswerQuestions; i++) {
            singleChoiceQuestionObject[i].setQuestion(singleChoiceQuestions[i]);
            singleChoiceQuestionObject[i].setOptions(singleChoiceQuestionOptions[i]);
            singleChoiceQuestionObject[i].setQuestionTextView(questionTextViews[i]);
            singleChoiceQuestionObject[i].setOptionsRadioGroup(optionRadioViews[i]);
        }

        TextView questionFiveTextView = findViewById(R.id.question_5);
        CheckBox questionFiveOptionOneCheckBox = findViewById(R.id.question_5_checkbox_1);
        CheckBox questionFiveOptionTwoCheckBox = findViewById(R.id.question_5_checkbox_2);
        CheckBox questionFiveOptionThreeCheckBox = findViewById(R.id.question_5_checkbox_3);
        CheckBox questionFiveOptionFourCheckBox = findViewById(R.id.question_5_checkbox_4);
        questionFiveTextView.setText(multipleChoiceQuestion[0]);
        questionFiveOptionOneCheckBox.setText(multipleChoiceQuestionOptions[0][0]);
        questionFiveOptionTwoCheckBox.setText(multipleChoiceQuestionOptions[0][1]);
        questionFiveOptionThreeCheckBox.setText(multipleChoiceQuestionOptions[0][2]);
        questionFiveOptionFourCheckBox.setText(multipleChoiceQuestionOptions[0][3]);

        TextView questionSixTextView = findViewById(R.id.question_6);
        questionSixTextView.setText(textInputQuestions[0][0]);

        TextView questionSevenTextView = findViewById(R.id.question_7);
        CheckBox questionSevenOptionOneCheckBox = findViewById(R.id.question_7_checkbox_1);
        CheckBox questionSevenOptionTwoCheckBox = findViewById(R.id.question_7_checkbox_2);
        CheckBox questionSevenOptionThreeCheckBox = findViewById(R.id.question_7_checkbox_3);
        CheckBox questionSevenOptionFourCheckBox = findViewById(R.id.question_7_checkbox_4);
        questionSevenTextView.setText(multipleChoiceQuestion[1]);
        questionSevenOptionOneCheckBox.setText(multipleChoiceQuestionOptions[1][0]);
        questionSevenOptionTwoCheckBox.setText(multipleChoiceQuestionOptions[1][1]);
        questionSevenOptionThreeCheckBox.setText(multipleChoiceQuestionOptions[1][2]);
        questionSevenOptionFourCheckBox.setText(multipleChoiceQuestionOptions[1][3]);

    }

    /**
     * Runs when the submit button is pressed
     * Updates TextView "quiz_summary" with the incorrect answers
     * @param view
     */
    public void submit(View view) {
        TextView summary = findViewById(R.id.quiz_summary);
        this.summary = createQuizSummary();
        summary.setText(this.summary);
    }

    /**
     * Runs when the Email Results button is pressed
     * @param view
     */
    public void sendEmail(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));//only email should handle this
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Quiz Results");
        emailIntent.putExtra(Intent.EXTRA_TEXT, createQuizSummary());
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Log.i("MainActivity", "Error opening intents");
        }
    }

    /**
     * Checks Results of all questions
     * Creates and returns a quiz summary of all the questions that were answered incorrectly
     * @return
     */
    private String createQuizSummary() {
        checkQuestionSix();
        for (int i = 0; i < numberOfSingleAnswerQuestions; i++) {
            correctAnswers[i] = singleChoiceQuestionObject[i].checkAnswer();
            if (correctAnswers[i]) {
                numberOfCorrect++;
            }
        }
        String summaryString = checkQuestionSeven();
        if (numberOfCorrect == numberOfSingleAnswerQuestions) {
            Toast.makeText(this, "Perfect Score!", Toast.LENGTH_SHORT).show();
            summaryString += "\nYou did perfect, Good job!";
        } else {
            String incorrectAnswers = "";
            for (int i = 0; i < correctAnswers.length; i++) {
                if (!correctAnswers[i]) {
                    incorrectAnswers += " Q" + (i + 1);
                }
            }
            Toast.makeText(this, incorrectAnswers + " Incorrect", Toast.LENGTH_SHORT).show();
            summaryString += "\nYou didnt pass, Try again" +
                    "\nIncorrect Questions:" + incorrectAnswers;
        }
        return summaryString;
    }

    /**
     * Checks Results for question one and displays toasts 
     * Triggered by Radiobutton under RadioGroup "question_1_options"
     * @param view
     */
    public void onRadioClickQuestionOne(View view) {
        //Referencing starts at 0
        int questionNumber = 0;
        if (singleChoiceQuestionObject[questionNumber].checkAnswer()) {
            Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks Results for question two and displays toasts 
     * Triggered by Radiobutton under RadioGroup "question_2_options"
     * @param view
     */
    public void onRadioClickQuestionTwo(View view) {
        //Referencing starts at 0
        int questionNumber = 1;
        if (singleChoiceQuestionObject[questionNumber].checkAnswer()) {
            Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks Results for question three and displays toasts 
     * Triggered by Radiobutton under RadioGroup "question_3_options"
     * @param view
     */
    public void onRadioClickQuestionThree(View view) {
        //Referencing starts at 0
        int questionNumber = 2;
        if (singleChoiceQuestionObject[questionNumber].checkAnswer()) {
            Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks Results for question four and displays toasts 
     * Triggered by Radiobutton under RadioGroup "question_4_options"
     * @param view
     */
    public void onRadioClickQuestionFour(View view) {
        //Referencing starts at 0
        int questionNumber = 3;
        if (singleChoiceQuestionObject[questionNumber].checkAnswer()) {
            Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks Results for question five and displays toasts 
     * Triggered by all CheckBox's under Question Five TextView
     * @param view
     */
    public void checkQuestionFive(View view) {
        //Referencing starts at 0
        int questionNumber = 4;
        //Check answer for question 5
        CheckBox question5Option1CheckBox = findViewById(R.id.question_5_checkbox_1);
        CheckBox question5Option2CheckBox = findViewById(R.id.question_5_checkbox_2);
        CheckBox question5Option3CheckBox = findViewById(R.id.question_5_checkbox_3);
        CheckBox question5Option4CheckBox = findViewById(R.id.question_5_checkbox_4);
        //If user selects "Blue", "Green", and "Purple", the answer is correct
        if (question5Option1CheckBox.isChecked() && question5Option2CheckBox.isChecked() && question5Option3CheckBox.isChecked() && !question5Option4CheckBox.isChecked()) {
            correctAnswers[questionNumber] = true;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            correctAnswers[questionNumber] = false;
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Checks Results for question six
     * Triggered by createQuizSummary()
     */
    private void checkQuestionSix() {
        //Referencing starts at 0
        int questionNumber = 5;
        EditText questionSevenAnswerEditText = findViewById(R.id.question_6_answer);
        correctAnswers[questionNumber] = questionSevenAnswerEditText.getText().toString().equals(textInputQuestions[0][1]);
    }

    /**
     * Question seven has no correct answer, however affects the summary wording
     * @return
     */
    private String checkQuestionSeven() {
        //Check the light sabre options selected for summary building
        String summaryString = "";
        EditText name = findViewById(R.id.name);
        CheckBox question6Option1CheckBox = findViewById(R.id.question_7_checkbox_1);
        CheckBox question6Option2CheckBox = findViewById(R.id.question_7_checkbox_2);
        CheckBox question6Option3CheckBox = findViewById(R.id.question_7_checkbox_3);
        CheckBox question6Option6CheckBox = findViewById(R.id.question_7_checkbox_4);
        boolean[] faction = {question6Option1CheckBox.isChecked(), question6Option2CheckBox.isChecked(), question6Option3CheckBox.isChecked(), question6Option6CheckBox.isChecked()};
        int lightSabreCount = 0;
        String factionHeader;
        for (int i = 0; i < 4; i++) {
            if (faction[i]) {
                lightSabreCount++;
            }
        }
        if (lightSabreCount > 1) {
            factionHeader = "Bounty Hunter ";
        } else if (question6Option6CheckBox.isChecked()) {
            factionHeader = "Darth ";
        } else {
            factionHeader = "Jedi Master ";
        }
        summaryString = "Thank you for taking the quiz, " + factionHeader + name.getText().toString();
        return summaryString;
    }

    /**
     * Object contains the information for each single choice question and logic for checking for correct answer
     */
    private class SingleChoiceQuestionObject {
        final int NUMBER_OF_OPTIONS = 4;
        private String question;
        private String[] options;
        private int answerID;
        private RadioGroup optionsRadioGroup;

        public boolean checkAnswer() {
            int checkedID = optionsRadioGroup.getCheckedRadioButtonId();
            return checkedID == answerID;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public void setOptions(String[] options) {
            this.options = options;
        }

        public void setQuestionTextView(TextView questionTextView) {
            questionTextView.setText(question);
        }

        private void setAnswerID(int answerID) {
            this.answerID = answerID;
        }

        /**
         * Randomly scrambles options so they do not appear in the same order every time
         * Sets the ID of the correct answer to be able to easily check for the correct answer after user selection
         * @param optionsRadioGroup
         */
        public void setOptionsRadioGroup(RadioGroup optionsRadioGroup) {
            this.optionsRadioGroup = optionsRadioGroup;
            Integer[] optionOrder = {0, 1, 2, 3};
            List<Integer> orderList = Arrays.asList(optionOrder);
            Collections.shuffle(orderList);
            orderList.toArray(optionOrder);
            for (int i = 0; i < NUMBER_OF_OPTIONS; i++) {
                ((RadioButton) optionsRadioGroup.getChildAt(i)).setText(options[optionOrder[i]]);
                if (optionOrder[i] == 0) {
                    setAnswerID(optionsRadioGroup.getChildAt(i).getId());
                }
            }
        }
    }
}