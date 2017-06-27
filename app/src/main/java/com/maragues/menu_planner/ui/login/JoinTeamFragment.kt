package com.maragues.menu_planner.ui.login

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.maragues.menu_planner.R

/**
 * A simple [DialogFragment] subclass.
 * Activities that contain this fragment must implement the
 * [JoinTeamFragment.OnJoinTeamInteractionListener] interface
 * to handle interaction events.
 * Use the [JoinTeamFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
internal class JoinTeamFragment : DialogFragment() {

    // TODO: Rename and change types of parameters
    private var groupName: String? = null

    private var listener: OnJoinTeamInteractionListener? = null

    fun onAcceptClicked() {
        listener?.onUserAcceptedInvitation(true)
    }

    fun onDeclineClicked() {
        listener?.onUserAcceptedInvitation(false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnJoinTeamInteractionListener) {
            listener = context as OnJoinTeamInteractionListener?
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnJoinTeamInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        groupName = arguments?.getString(ARG_GROUP_NAME)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_join_team, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setMessage("Join $groupName")
                .setNeutralButton(R.string.fragment_join_team_decline, { dialog, which -> onDeclineClicked() })
                .setPositiveButton(R.string.fragment_join_team_join, { dialog, which -> onAcceptClicked() })
                .create()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnJoinTeamInteractionListener {
        // TODO: Update argument type and name
        fun onUserAcceptedInvitation(accepted: Boolean)
    }

    internal companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_GROUP_NAME = "group_name"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * *
         * @return A new instance of fragment JoinTeamFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(groupName: String): JoinTeamFragment {
            val fragment = JoinTeamFragment()
            val args = Bundle()
            args.putString(ARG_GROUP_NAME, groupName)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
