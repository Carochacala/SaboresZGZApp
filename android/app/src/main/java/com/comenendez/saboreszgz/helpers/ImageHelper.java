package com.comenendez.saboreszgz.helpers;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
public class ImageHelper {

        /**
         * Carga una imagen desde una URL en un ImageView
         * @param context Contexto de la aplicación
         * @param url URL de la imagen (puede ser null o vacío)
         * @param imageView ImageView donde mostrar la imagen
         */
        public static void cargarImagen(Context context, String url, ImageView imageView) {
            if (url != null && !url.isEmpty()) {
                Glide.with(context)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_gallery)
                        .into(imageView);
            } else {
                imageView.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }

        /**
         * Carga una imagen con placeholder personalizado
         */
        public static void cargarImagen(Context context, String url, ImageView imageView, int placeholderResId) {
            if (url != null && !url.isEmpty()) {
                Glide.with(context)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(placeholderResId)
                        .error(placeholderResId)
                        .into(imageView);
            } else {
                imageView.setImageResource(placeholderResId);
            }
        }

        /**
         * Carga una imagen con tamaño personalizado
         */
        public static void cargarImagen(Context context, String url, ImageView imageView, int width, int height) {
            if (url != null && !url.isEmpty()) {
                Glide.with(context)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(width, height)
                        .centerCrop()
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_gallery)
                        .into(imageView);
            } else {
                imageView.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }
    }

