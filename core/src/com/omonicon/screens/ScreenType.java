package com.omonicon.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by Jakov on 8.10.2015..
 */
public enum ScreenType {

    /*NO_SCREEN {
        @Override
        protected Screen getScreenInstance() {
            return null;
        }
    },*/

    SPLASH_SCREEN_OCEAN_MEDIA {
        @Override
        protected Screen getScreenInstance() {
            SplashScreen ssOceanMedia = new SplashScreen("images/oceanmedialogo.png", ScreenType.SPLASH_SCREEN_OCEAN_MEDIA);
            Color bgColor = new Color(0.0353f, 0.22746f, 0.3687f, 1);
            ssOceanMedia.setBgColor(bgColor);
            ssOceanMedia.setShowTimeSec(3.0f);
            ssOceanMedia.setNextScreenToShow(ScreenType.SPLASH_SCREEN_BAD_LOGIC_GAMES);
            return ssOceanMedia;
        }
    },

    SPLASH_SCREEN_BAD_LOGIC_GAMES {
        @Override
        protected Screen getScreenInstance() {
            SplashScreen ssBadLogicGames = new SplashScreen("images/badlogic.jpg", ScreenType.SPLASH_SCREEN_BAD_LOGIC_GAMES);
            ssBadLogicGames.setBgColor(Color.BLACK);
            ssBadLogicGames.setShowTimeSec(4.0f);
            //ssBadLogicGames.setNextScreenToShow(ScreenType.MAIN_MENU_SCREEN);
            ssBadLogicGames.setNextScreenToShow(ScreenType.INTRO_SCREEN);
            return ssBadLogicGames;
        }
    },

    INTRO_SCREEN {
        @Override
        protected Screen getScreenInstance() {
            IntroScreen isIntroScreen = new IntroScreen("images/IntroScreen.png", ScreenType.INTRO_SCREEN);
            isIntroScreen.setBgColor(Color.PURPLE);
            isIntroScreen.setShowTimeSec(4.0f);
            isIntroScreen.setNextScreenToShow(ScreenType.CREDITS_SCREEN);
            return isIntroScreen;
        }
    },

    CREDITS_SCREEN {
        @Override
        protected Screen getScreenInstance() {
            SplashScreen ssBadLogicGames = new SplashScreen("images/CreditsScreen.png", ScreenType.CREDITS_SCREEN);
            ssBadLogicGames.setBgColor(Color.GREEN);
            ssBadLogicGames.setShowTimeSec(4.0f);
            ssBadLogicGames.setNextScreenToShow(ScreenType.MAIN_MENU_SCREEN);
            return ssBadLogicGames;
        }
    },

    MAIN_MENU_SCREEN {
        @Override
        protected Screen getScreenInstance() {
            //MainMenuScreen mmsMainMenu = new MainMenuScreen(ScreenType.SPLASH_SCREEN_BAD_LOGIC_GAMES);
            //MainMenuScreen mmsMainMenu = new MainMenuScreen(ScreenType.CREDITS_SCREEN);
            MainMenuScreen mmsMainMenu = new MainMenuScreen(ScreenType.CREDITS_SCREEN, ScreenType.MAIN_MENU_SCREEN);
            mmsMainMenu.setShowTimeSec(4.0f);
            mmsMainMenu.setNextScreenToShow(ScreenType.GAME_SCREEN);
            mmsMainMenu.setBgColor(Color.RED);
            return mmsMainMenu;
        }
    },

    GAME_SCREEN {
        @Override
        protected Screen getScreenInstance() {
            //MainMenuScreen mmsMainMenu = new MainMenuScreen(ScreenType.SPLASH_SCREEN_BAD_LOGIC_GAMES);
            GameScreen gsGameScreen = new GameScreen();
            return gsGameScreen;
        }
    };

    protected abstract Screen getScreenInstance();
}

