package com.yeket.music.bridge.ui.views.select_genres

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.igalata.bubblepicker.adapter.BubblePickerAdapter
import com.igalata.bubblepicker.model.BubbleGradient
import com.igalata.bubblepicker.model.PickerItem
import com.yeket.music.R
import com.yeket.music.bridge.infrastructure.di.components.DaggerPresenterComponent
import com.yeket.music.bridge.infrastructure.di.modules.ContextModule
import com.yeket.music.bridge.infrastructure.di.modules.ViewModule
import com.yeket.music.bridge.ui.views.BaseFragment
import com.yeket.music.bridge.ui.views.LuresApplication
import com.yeket.music.bridge.ui.views.profile.PersonalProfileFragment.FAVORITE_GENRES
import com.yeket.music.bridge.ui.views.profile.PersonalProfileFragment.GENRES
import kotlinx.android.synthetic.main.fragment_select_genres.*
import java.util.*

class SelectGenresFragment : BaseFragment(), ChooseGenresContract.View{

    lateinit var presenter : ChooseGenresContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_select_genres, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = DaggerPresenterComponent
                .builder()
                .singletonComponent((activity.application as? LuresApplication)?.getSingletonComponent())
                .viewModule(ViewModule(this))
                .contextModule(ContextModule(activity))
                .build()
                .chooseGenresPresenter();

        picker.maxSelectedCount = 3

        val genres : ArrayList<String>  = arguments.getStringArrayList(GENRES)
        val favoriteGenres : ArrayList<String>  = arguments.getStringArrayList(FAVORITE_GENRES)

        val colors = resources.obtainTypedArray(R.array.colors)

        picker.adapter = object : BubblePickerAdapter {

            override val totalCount = genres.size

            override fun getItem(position: Int): PickerItem = PickerItem().apply {

                if(favoriteGenres.contains(genres[position])){
                    isSelected = true
                }

                title = genres[position]
                gradient = BubbleGradient(colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL)
                textColor = ContextCompat.getColor(activity, android.R.color.white)

            }
        }

        picker.bubbleSize = 70

        colors.recycle()

        save_button.setOnClickListener({
            presenter.saveClicked(picker.selectedItems.map({item -> item?.title}).toList())
        })
    }

    override fun onResume() {
        super.onResume()
        picker.onResume()
    }

    override fun onPause() {
        super.onPause()
        picker.onPause()
    }

    override fun goNextScreen() {
        if (fragmentManager.backStackEntryCount > 0) { fragmentManager.popBackStack() }
    }

    override fun getRootLayout(): ViewGroup = selected_genres_layout

}
