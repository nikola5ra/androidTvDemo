package interventure.com.androidtv

import android.content.Context
import android.support.v17.leanback.media.PlaybackTransportControlGlue
import android.support.v17.leanback.media.PlayerAdapter
import android.support.v17.leanback.widget.Action
import android.support.v17.leanback.widget.ArrayObjectAdapter
import android.support.v17.leanback.widget.PlaybackControlsRow
import android.widget.Toast

/**
 * Created by Nikola Petrovic on 8/27/18.
 */
class CustomPlaybackTransportControlGlue<T : PlayerAdapter>
(context: Context, impl: T?, listener: ActionListener) : PlaybackTransportControlGlue<T>(context, impl) {

    private var mActionListener: ActionListener? = null

    private val mRepeatAction: PlaybackControlsRow.RepeatAction? = null
    private val mPipAction: PlaybackControlsRow.PictureInPictureAction? = null
    private var mThumbsUpAction: PlaybackControlsRow.ThumbsUpAction? = null
    private var mThumbsDownAction: PlaybackControlsRow.ThumbsDownAction? = null
    private var mSkipPreviousAction: PlaybackControlsRow.SkipPreviousAction? = null
    private var mSkipNextAction: PlaybackControlsRow.SkipNextAction? = null
    private var mFastForwardAction: PlaybackControlsRow.FastForwardAction? = null
    private var mRewindAction: PlaybackControlsRow.RewindAction? = null

    init {
        mActionListener = listener

        mThumbsUpAction = PlaybackControlsRow.ThumbsUpAction(context)
        mThumbsDownAction = PlaybackControlsRow.ThumbsDownAction(context)
        mSkipPreviousAction = PlaybackControlsRow.SkipPreviousAction(context)
        mSkipNextAction = PlaybackControlsRow.SkipNextAction(context)
        mFastForwardAction = PlaybackControlsRow.FastForwardAction(context)
        mRewindAction = PlaybackControlsRow.RewindAction(context)
    }

    override fun onCreatePrimaryActions(primaryActionsAdapter: ArrayObjectAdapter) {
        // Order matters, super.onCreatePrimaryActions() will create the play / pause action.
        // Will display as follows:
        // play/pause, previous, rewind, fast forward, next
        //   > /||      |<        <<        >>         >|
        super.onCreatePrimaryActions(primaryActionsAdapter)
        primaryActionsAdapter.add(mSkipPreviousAction)
        primaryActionsAdapter.add(mRewindAction)
        primaryActionsAdapter.add(mFastForwardAction)
        primaryActionsAdapter.add(mSkipNextAction)
    }

    override fun onCreateSecondaryActions(adapter: ArrayObjectAdapter?) {
        super.onCreateSecondaryActions(adapter)
        adapter!!.add(mThumbsDownAction)
        adapter.add(mThumbsUpAction)
    }

    override fun onActionClicked(action: Action) {
        when {
            action === mRewindAction -> {
                seekTo(currentPosition - 5000)
            }
            action === mFastForwardAction -> {
                seekTo(currentPosition + 5000)
            }
            action === mThumbsDownAction -> {
                Toast.makeText(context, "On thumbs down", Toast.LENGTH_LONG).show()
            }
            action === mThumbsUpAction -> {
                Toast.makeText(context, "On thumbs up", Toast.LENGTH_LONG).show()
            }
            else -> // The superclass handles play/pause and delegates next/previous actions to abstract methods,
                // so those two methods should be overridden rather than handling the actions here.
                super.onActionClicked(action)
        }
    }

    override fun next() {
        mActionListener?.onActionNext()
    }

    override fun previous() {
        mActionListener?.onActionPrevious()
    }
}