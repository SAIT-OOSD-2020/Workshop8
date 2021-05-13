package com.example.workshop8.ui.home;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.workshop8.R;
import com.example.workshop8.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ImageView ivLogo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ivLogo = root.findViewById(R.id.ivLogo);

        ObjectAnimator translateX = ObjectAnimator.ofFloat(ivLogo, "translationX", -1000f, 0);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(ivLogo, "translationY", 1000f, 0);
        translateX.setDuration(2000);
        translateX.start();
        translateY.setDuration(2000);
        translateY.start();



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}