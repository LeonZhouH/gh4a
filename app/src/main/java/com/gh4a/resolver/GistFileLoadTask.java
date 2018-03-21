package com.gh4a.resolver;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.gh4a.ServiceFactory;
import com.gh4a.activities.GistActivity;
import com.gh4a.activities.GistViewerActivity;
import com.gh4a.utils.ApiHelpers;
import com.gh4a.utils.Optional;
import com.meisolsson.githubsdk.service.gists.GistService;

import io.reactivex.Single;

public class GistFileLoadTask extends UrlLoadTask {
    private final String mGistId;
    private final String mFileName;

    public GistFileLoadTask(FragmentActivity activity, String gistId, String fileName) {
        super(activity);
        mGistId = gistId;
        mFileName = fileName;
    }

    @Override
    protected Single<Optional<Intent>> getSingle() {
        GistService service = ServiceFactory.get(GistService.class, false);
        return service.getGist(mGistId)
                .map(ApiHelpers::throwOnFailure)
                .map(gist -> Optional.ofWithNull(gist.files().get(mFileName)))
                .map(opt -> opt.map(file -> GistViewerActivity.makeIntent(mActivity, mGistId, file.filename())))
                .map(opt -> opt.or(() -> GistActivity.makeIntent(mActivity, mGistId)));
    }
}
