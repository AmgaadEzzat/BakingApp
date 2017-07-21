package com.example.savior.bakingapp;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savior.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import static com.example.savior.bakingapp.StepsActivity.PANES;
import static com.example.savior.bakingapp.StepsActivity.POSITION;
import static com.example.savior.bakingapp.adapter.Step_Adapter.STEPS;

/**
 * Created by savior on 7/18/2017.
 */

public class StepsDetailsFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener {


    private TrackSelector trackSelector;
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private TextView mDescription;
    private Button mPrevious;
    private Button mNext;
    private ArrayList<Step> mStep;
    private int mIndex;
    private int mPosition;
    private boolean isTab;
    private static long wPosition = 0;
    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private boolean autoPlay = false;
    private int currentWindow;
    private long playbackPosition;


    public StepsDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {

            mStep = savedInstanceState.getParcelableArrayList(STEPS);
            isTab = savedInstanceState.getBoolean(PANES);
            mPosition = savedInstanceState.getInt(POSITION);

            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 1);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 1);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);
        }

        View view = inflater.inflate(R.layout.step_details_fragment, container, false);
        mNext = (Button) view.findViewById(R.id.next);
        mPrevious = (Button) view.findViewById(R.id.previous);
        mPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.exo_player);
        mDescription = (TextView) view.findViewById(R.id.description);


        mNext.setOnClickListener(this);
        mPrevious.setOnClickListener(this);


        mPosition = getArguments().getInt(POSITION);
        mIndex = mPosition ;
        isTab = getArguments().getBoolean(PANES);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isTab) {
            theTab();
        } else {
            thePhone();
        }
    }


    private void playVideo(int index) {
        mPlayerView.setVisibility(View.VISIBLE);
        mPlayerView.requestFocus();
        String videoUrl = mStep.get(index).getVideoUrl();
        String thumbNailUrl = mStep.get(index).getThumbnailUrl();
        if (!videoUrl.isEmpty()) {
            initPlayer(Uri.parse(videoUrl));

        } else if (!thumbNailUrl.isEmpty()) {
           initPlayer(Uri.parse(thumbNailUrl));


        } else {
            mPlayerView.setVisibility(View.GONE);
        }
    }
    public void theTab() {
        mPlayerView.setVisibility(View.VISIBLE);
        mDescription.setVisibility(View.VISIBLE);
        mStep = getArguments().getParcelableArrayList(STEPS);
        assert mStep != null;
        mDescription.setText(mStep.get(mIndex).getDescription());
        playVideo(mIndex);
    }


    private void thePhone() {
        theTab();
        isLandscape();
        mPrevious.setVisibility(View.VISIBLE);
        mNext.setVisibility(View.VISIBLE);
    }

    void isLandscape() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            hideSystemUi();
    }



    private void initPlayer(Uri mediaUri) {
        mExoPlayer = null;

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity(),
                null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF);

        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);


        mExoPlayer.addListener(this);

        mPlayerView.setPlayer(mExoPlayer);
        mExoPlayer.setPlayWhenReady(true);

        mExoPlayer.seekTo(currentWindow, playbackPosition);

        String userAgent = Util.getUserAgent(getActivity(), "Baking App");

        MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                new DefaultDataSourceFactory(getActivity(), BANDWIDTH_METER,
                        new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER)),
                new DefaultExtractorsFactory(), null, null);

        mExoPlayer.prepare(mediaSource);
        CheckPlayer(wPosition, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        workPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        workPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        workPlayer();
    }


    private void workPlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                if (mIndex < mStep.size() - 1) {
                    int index = ++mIndex;
                    mDescription.setText(mStep.get(index).getDescription());
                    CheckPlayer(0, false);
                    playVideo(index);
                } else {
                    Toast.makeText(getActivity(), R.string.the_end, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.previous:
                if (mIndex > 0) {
                    int index = --mIndex;
                    mDescription.setText(mStep.get(index).getDescription());
                    CheckPlayer(0, false);
                    playVideo(index);
                } else {
                    Toast.makeText(getActivity(), R.string.back_start, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void CheckPlayer(long position, boolean playWhenReady) {
        this.wPosition = position;
        mExoPlayer.seekTo(position);
        mExoPlayer.setPlayWhenReady(playWhenReady);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mExoPlayer != null) {
            outState.putLong(PLAYBACK_POSITION, playbackPosition);
            outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
            outState.putBoolean(AUTOPLAY, autoPlay);
        }
        outState.putParcelableArrayList(STEPS, mStep);
        outState.putBoolean(PANES, isTab);
        outState.putInt(POSITION, mPosition);

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            Toast.makeText(getActivity(), "Working", Toast.LENGTH_LONG).show();
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
        }
        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
            wPosition = mExoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        if (errorString != null) {
            Toast.makeText(getActivity(), errorString, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }


}

