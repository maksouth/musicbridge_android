package com.yeket.music.bridge.ui.adapters;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yeket.music.R;
import com.yeket.music.bridge.constants.Dimensions;
import com.yeket.music.bridge.datasource.music.MusicDataSource;
import com.yeket.music.bridge.infrastructure.callbacks.LoggerFailureCallback;
import com.yeket.music.bridge.infrastructure.utils.DateUtils;
import com.yeket.music.bridge.models.Artist;
import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.ui.ui_components.SizeConverter;
import com.yeket.music.bridge.ui.ui_components.TagTextView;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSwipeableCardAdapter extends ArrayAdapter<LuresUser> {

    private MusicDataSource musicDataSource;
    private Location currentUserLocation;

    @Inject
    public UserSwipeableCardAdapter(@NonNull Context context,
                                    @NotNull MusicDataSource musicDataSource,
                                    @NonNull LuresUser currentUser) {
        super(context, 0);
        this.musicDataSource = musicDataSource;
        currentUserLocation = currentUser.getLocation();
    }

    @NonNull
    @Override
    public View getView(int position, View contentView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.user_profile_card, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        LuresUser data = getItem(position);

        if(data == null) return contentView;

        musicDataSource.getFavoriteGenres(data.getId(), holder::setGenres, LoggerFailureCallback.EMPTY);

        musicDataSource.getArtists(data.getId(), 5, holder::addArtists, LoggerFailureCallback.EMPTY);

        String age = getAge(data.getBirthdate());
        final String name = getName(data.getDisplayName());
        String distance = getDistance(data.getLocation(), currentUserLocation);

        holder.setAge(age);
        holder.setName(name);
        holder.setDistance(distance);
        holder.setImage(data.getImageUri());
        holder.setAboutYou(data.getAboutYou());

        return contentView;
    }

    private String getName(String original){
        return original.substring(0, original.indexOf(" "));
    }

    private String getAge(long birthdate){

        Date userBDay = new Date(birthdate);
        Date currentUserBDay = new Date();
        int years = DateUtils.getDiffYears(userBDay, currentUserBDay);

        return years + getContext().getString(R.string.years);
    }

    private String getDistance(Location pointA, Location pointB){

        if(pointA == null || pointB == null) {
            return "";
        }
        float distance = ((int)(pointA.distanceTo(pointB)/100))/10;

        return distance + getContext().getString(R.string.km_away);
    }

    private class ViewHolder {

        private Context context;
        private TextView name;
        private TextView age;
        private LinearLayout artistsLayout;
        private CardView artistListCard;
        private TextView distance;
        private TextView aboutYou;
        private ImageView image;
        private LinearLayout genresLayout;

        public ViewHolder(View view) {

            this.context = view.getContext();

            this.name = (TextView) view.findViewById(R.id.name_label);
            this.age = (TextView) view.findViewById(R.id.age_label);
            this.image = (ImageView) view.findViewById(R.id.profile_image);
            this.distance = (TextView) view.findViewById(R.id.location_label);
            this.artistsLayout = (LinearLayout) view.findViewById(R.id.artists_list);
            this.aboutYou = (TextView) view.findViewById(R.id.about_you_label);
            this.artistListCard = (CardView) view.findViewById(R.id.artists_list_card);

            genresLayout = (LinearLayout) view.findViewById(R.id.genre_tags_list);
        }

        public void setImage(String imageUri){
            Picasso.with(context).load(imageUri).into(image);
        }

        public void setName(final String nameStr){
            name.setText(nameStr);
        }

        public void setAge(final String ageStr){
            age.setText(ageStr);
        }

        public void setDistance(String distanceStr ){
            distance.setText(distanceStr);
        }

        public void setAboutYou(String aboutYouStr){
            aboutYou.setText(aboutYouStr);
        }

        public void addArtists(final List<Artist> artists){

            if(artists != null && artists.size() != 0) {
                artistListCard.setVisibility(View.VISIBLE);
                for (Artist artist : artists) {
                    final String imageUri = artist.getImageUrl();
                    final ImageView imageView = new CircleImageView(context);
                    imageView.post(() -> {
                        int pixels = SizeConverter.toPx(Dimensions.ARTIST_IMAGE_SIZE, context);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pixels, pixels);
                        params.leftMargin = Dimensions.ARTIST_IMAGE_LEFT_MARGIN;
                        imageView.setLayoutParams(params);
                        imageView.setScaleType(CircleImageView.ScaleType.CENTER_CROP);
                        imageView.setImageResource(R.drawable.boy_icon_normal);

                        Picasso.with(context).load(imageUri).into(imageView);
                    });

                    artistsLayout.addView(imageView);
                }
            }
        }

        public void setGenres(final List<String> genres){

            if(genres.size()>=0) {
                final TagTextView tagLabel = new TagTextView(getContext());
                tagLabel.post(() -> tagLabel.setText(genres.get(0)));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                tagLabel.setLayoutParams(params);

                genresLayout.addView(tagLabel);
            }

            for(int i = 1; i < genres.size(); i++){
                final int finalI = i;
                final TagTextView tagLabel = new TagTextView(getContext());
                tagLabel.post(() -> tagLabel.setText(genres.get(finalI)));

                int leftMargin = SizeConverter.toPx(8, context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(leftMargin, 0, 0, 0);
                tagLabel.setLayoutParams(params);

                genresLayout.addView(tagLabel);
            }
        }
    }

}

