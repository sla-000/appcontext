package cc.mobylabs.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Main context storage
 *
 * How to use in App class:
 * <pre>
 *   public class TaasApp extends Application {
 *     public final static String TAG = "Taas";
 *
 *     @Override
 *     public void onCreate() {
 *       super.onCreate();
 *
 *       AppContext.getInstance().set( this );
 *       ...
 * </pre>
 *
 * How to use from other classes:
 * <pre>
 *   final Context context = AppContext.getInstance().getOrNull();
 *   if( context == null ) {
 *     throw new RuntimeException( "context == null, add AppContext.getInstance().set( this ) to onCreate of your main app class" );
 *   }
 *
 *   mLogDb = Room.databaseBuilder( context, LogDb.class, "log.db").build();
 *   ...
 * </pre>
 *
 * or shorter with runtime exception:
 *
 * <pre>
 *   mLogDb = Room.databaseBuilder( AppContext.getInstance().getOrThrow(), LogDb.class, "log.db").build();
 *   ...
 * </pre>
 */
public class AppContext {

	/**
	 * Get instance of singleton
	 */
	@NonNull
	public static synchronized AppContext getInstance() {
		if( mAppContext == null ) {
			mAppContext = new AppContext();
		}

		return mAppContext;
	}

	/**
	 * Set app context.
	 *
	 * Add AppContext.getInstance().set( this ) to onCreate of your main app class
	 */
	public void set( @NonNull final Context context ) {
		mContext = new WeakReference<>( context );
	}

	/**
	 * Get app context
	 *
	 * If null you must add AppContext.getInstance().set( this ) to onCreate of your main app class
	 *
	 * Typical usage:
	 * <pre>
		final Context context = AppContext.getInstance().get();

		if( context == null ) {
			throw new RuntimeException( "context == null, add AppContext.getInstance().set( this ) to onCreate of your main app class" );
		}
	 * </pre>
	 */
	@Nullable
	public Context getOrNull() {
		if( mContext == null ) {
			return null;
		}

		return mContext.get();
	}

	/**
	 * Get app context or throw RuntimeException if can't get usable context
	 *
	 * If null you must add AppContext.getInstance().set( this ) to onCreate of your main app class
	 */
	@NonNull
	public Context getOrThrow() throws RuntimeException {
		if( mContext == null ) {
			throw new RuntimeException( "context == null, add AppContext.getInstance().set( this ) at first lines of your main app class onCreate method" );
		}

		final Context context = mContext.get();
		if( context == null ) {
			throw new RuntimeException( "App context somehow become null" );
		}

		return context;
	}

	@Nullable
	private WeakReference<Context> mContext;

	@Nullable
	private static AppContext mAppContext;
}
