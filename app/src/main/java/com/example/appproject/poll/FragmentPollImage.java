package com.example.appproject.poll;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.example.appproject.model.poll.Answer;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class FragmentPollImage extends Fragment {

    PollImageViewModel viewModel;
    MaterialTextView imageQuestion;
    MaterialButton backBtn;
    MaterialButton uploadBtn;
    MaterialButton finishBtn;
    ProgressBar progressBar;
    ActivityResultLauncher<String> galleryActivityResultLauncher;
    ShapeableImageView image;
    Bitmap bitMap;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PollImageViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.main_menu_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poll_image, container, false);
        image = view.findViewById(R.id.imgpoll_image);
        imageQuestion = view.findViewById(R.id.imgpoll_question_title);
        progressBar = view.findViewById(R.id.imgpoll_progress_bar);
        progressBar.setVisibility(View.GONE);
        backBtn = view.findViewById(R.id.imgpoll_btn_back);
        backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    try {
                        if (result != null)
                            bitMap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), result);
                    } catch (IOException ignored) {
                    }
                    if (result != null)
                        image.setImageBitmap(bitMap);
                }
        );
        uploadBtn = view.findViewById(R.id.imgpoll_btn_upload);
        uploadBtn.setOnClickListener(v -> {
            openGallery();
        });
        String imagePollQuestionId = FragmentPollImageArgs.fromBundle(getArguments()).getImagePollQuestionId();
        Model.instance.getPollQuestion(imagePollQuestionId, pollQuestion -> {
            viewModel.setPollQuestion(pollQuestion);
            Model.instance.getMainThread().post(()->{
                imageQuestion.setText(viewModel.getPollQuestion().getPollQuestion());
            });
            Model.instance.getAllAnswersByUserAndPollIds(MyApplication.getUserKey(),viewModel.getPollQuestion().getPollId(), map -> {
                if (map.get(viewModel.getPollQuestion().getPollQuestionId()) != null) {
                    General.progressBarOn(getActivity(),container,progressBar);
                    Model.instance.getMainThread().post(() -> Picasso.get().load(map.get(viewModel.getPollQuestion().getPollQuestionId()).getAnswer()).memoryPolicy(MemoryPolicy.NO_CACHE).into(image, new Callback() {
                        @Override
                        public void onSuccess() {
                            General.progressBarOff(getActivity(),container,progressBar);
                        }

                        @Override
                        public void onError(Exception e) {
                            General.progressBarOff(getActivity(),container,progressBar);
                        }
                    }));
                }
                else{
                    Model.instance.getMainThread().post(()->image.setImageResource(R.drawable.noimage1));
                }
            });
        });
        finishBtn = view.findViewById(R.id.imgpoll_btn_finish);
        finishBtn.setOnClickListener(v -> {
            General.progressBarOn(getActivity(),container,progressBar);
            if (bitMap == null) {
                Model.instance.getAllAnswersByUserAndPollIds(MyApplication.getUserKey(),viewModel.getPollQuestion().getPollId(), map -> {
                    if (map.get(viewModel.getPollQuestion().getPollQuestionId()) != null) {
                        Model.instance.savePollAnswersOnDb(MyApplication.getUserKey(), viewModel.getPollQuestion().getPollId(), () -> {
                            General.progressBarOff(getActivity(),container,progressBar);
                            Model.instance.getMainThread().post(() -> {
                                Navigation.findNavController(backBtn).navigate(FragmentPollImageDirections.actionFragmentPollImageToFragmentHomeScreen());
                            });
                        });
                    } else {
                        General.progressBarOff(getActivity(),container,progressBar);
                        Snackbar.make(getView(), getString(R.string.no_image_upload), Snackbar.LENGTH_SHORT).show();
                    }

                });

            } else {
                Model.instance.saveImage(bitMap, MyApplication.getUserKey() + viewModel.getPollQuestion().getPollQuestionId() + ".jpg", "users_poll_images/", url -> {
                    if (url == null) {
                        Snackbar.make(getView(), getString(R.string.image_upload_failed), Snackbar.LENGTH_SHORT).show();
                    } else {
                        Model.instance.getAllAnswersByUserAndPollIds(MyApplication.getUserKey(),viewModel.getPollQuestion().getPollId(), map -> {
                            if (map.get(viewModel.getPollQuestion().getPollQuestionId()) != null) {
                                Model.instance.savePollAnswersOnDb(MyApplication.getUserKey(), viewModel.getPollQuestion().getPollId(), () -> {
                                    Model.instance.getMainThread().post(() -> {
                                        Navigation.findNavController(backBtn).navigate(FragmentPollImageDirections.actionFragmentPollImageToFragmentHomeScreen());
                                    });
                                });
                            } else {
                                Map<String, Answer> newMap = new HashMap<>();
                                newMap.put(viewModel.getPollQuestion().getPollQuestionId(), new Answer(UUID.randomUUID().toString(), MyApplication.getUserKey(), viewModel.pollQuestion.getPollId(), viewModel.getPollQuestion().getPollQuestionId(), url));
                                Model.instance.savePollAnswersOnLocalDb(newMap, () -> {
                                    Model.instance.savePollAnswersOnDb(MyApplication.getUserKey(), viewModel.getPollQuestion().getPollId(), () -> {
                                        Model.instance.getMainThread().post(() -> {
                                            Navigation.findNavController(backBtn).navigate(FragmentPollImageDirections.actionFragmentPollImageToFragmentHomeScreen());
                                        });
                                    });
                                });
                            }

                        });

                    }
                });
            }
        });

        return view;
    }


    private void openGallery() {
        galleryActivityResultLauncher.launch("image/*");
    }
}