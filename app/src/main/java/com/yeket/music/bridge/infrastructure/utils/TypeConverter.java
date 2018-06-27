package com.yeket.music.bridge.infrastructure.utils;

import com.android.internal.util.Predicate;
import com.yeket.music.bridge.models.Track;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.SavedTrack;

public class TypeConverter {

    private static Track getTrack(kaaes.spotify.webapi.android.models.Track track){
        Track luresTrack = new Track();

        luresTrack.setSpotifyId(track.id);
        luresTrack.setName(track.name);
        luresTrack.setImageUrl(track.album.images.get(0).url);
        luresTrack.setPreviewUrl(track.preview_url);
        luresTrack.setDurationMs(track.duration_ms);

        return luresTrack;
    }

    public static List<Track> getTracks(List<kaaes.spotify.webapi.android.models.Track> tracks){
        List<Track> luresTracks = new ArrayList<>();

        for(int i = 0; i < tracks.size(); i++){
            luresTracks.add(getTrack(tracks.get(i)));
        }

        return luresTracks;
    }

    private static com.yeket.music.bridge.models.Artist getArtist(Artist artist){

        com.yeket.music.bridge.models.Artist luresArtist = new com.yeket.music.bridge.models.Artist();

        luresArtist.setSpotifyId(artist.id);
        luresArtist.setName(artist.name);
        luresArtist.setImageUrl(artist.images.get(0).url);

        return luresArtist;
    }

    public static List<com.yeket.music.bridge.models.Artist> getArtists(List<Artist> artists){
        List<com.yeket.music.bridge.models.Artist> luresArtists = new ArrayList<>();

        for(Artist artist : artists){
            luresArtists.add(getArtist(artist));
        }

        return luresArtists;
    }

    public static List<Track> getTracksFromSaved(List<SavedTrack> list){
        List<Track> tracks = new ArrayList<>();
        Track temp;

        for(SavedTrack savedTrack : list){
            temp = getTrack(savedTrack.track);
            tracks.add(temp);
        }

        return tracks;
    }

    public static Track getLastValidTrack(List<SavedTrack> list, Predicate<SavedTrack> predicate){

        for(int i = list.size() - 1; i >= 0; i--){
            if(predicate.apply(list.get(i))) {
                return getTrack(list.get(i).track);
            }
        }

        return null;
    }

    public static Track getTrackFromSaved(SavedTrack savedTrack){

        return getTrack(savedTrack.track);
    }

}
