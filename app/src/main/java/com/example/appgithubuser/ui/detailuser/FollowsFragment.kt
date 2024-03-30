package com.example.appgithubuser.ui.detailuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithubuser.data.remote.response.ItemsItem
import com.example.appgithubuser.databinding.FragmentFollowsBinding
import com.example.appgithubuser.ui.main.UserAdapter

class FollowsFragment : Fragment() {

    private lateinit var binding: FragmentFollowsBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FollowsViewModel
    private lateinit var username: String

    companion object {
        private const val ARG_POSITION = "arg_position"
        private const val ARG_USERNAME = "arg_username"

        fun newInstance(position: Int, username: String): FollowsFragment {
            val fragment = FollowsFragment()
            val args = Bundle().apply {
                putInt(ARG_POSITION, position)
                putString(ARG_USERNAME, username)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString(ARG_USERNAME) ?: ""
        adapter = UserAdapter { user ->

        }

        with(binding) {
            rvFollows.layoutManager = LinearLayoutManager(requireContext())
            rvFollows.adapter = adapter
        }

        viewModel = ViewModelProvider(requireActivity()).get(FollowsViewModel::class.java)
        showLoading(true)

        viewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            showLoading(isLoading)
        })

        if (arguments?.getInt(ARG_POSITION) == 0) {
            viewModel.followers.observe(viewLifecycleOwner, { followers ->
                followers?.let {
                    showFollowers(it)
                }
            })
            viewModel.getFollowers(username)
        } else {
            viewModel.following.observe(viewLifecycleOwner, { following ->
                following?.let {
                    showFollowing(it)
                }
            })
            viewModel.getFollowing(username)
        }
    }

    private fun showFollowers(followers: List<ItemsItem>) {
        adapter.submitList(followers)
    }

    private fun showFollowing(following: List<ItemsItem>) {
        adapter.submitList(following)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}