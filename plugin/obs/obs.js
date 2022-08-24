// reveal.js integration with OBS
const RevealOBS = (() => {
    const obs = new OBSWebSocket();
    obs.connect({address: 'localhost:4444', password: '952964'})
        .then(() => console.log("We are connected to OBS!"))
        .catch(err => console.log(err))

    return {
        init: () => {
            /*
            Once reveal.js is ready we iterate all scene names set on the slides
            to check if any of them are wrong. In case a name doesn't correspond
            with an OBS scene, we show a helpful error screen.
            */
            Reveal.addEventListener('ready', () => {
                obs.send('GetSceneList').then(sceneList => {
                    const slideScenes = [...new Set(Reveal.getSlides()
                        .map(slide => slide.dataset.scene)
                        .filter(scene => scene !== undefined))]

                    const availableScenes = sceneList.scenes.map(scene => scene.name)

                    const fuzzy = FuzzySet(availableScenes)

                    const notFoundScenes = slideScenes
                        .filter(scene => !availableScenes.includes(scene))
                        .map(scene => {
                            let match = fuzzy.get(scene)

                            if (match === null) return scene;
                            else return scene + " (maybe you mean '" + match[0][1] + "')";
                        })

                    if (notFoundScenes.length) {
                        Reveal.getRevealElement().innerHTML = '<section style="margin: 40px" data-state="alert">' +
                            '<h1>Error!</h1>' +
                            '<p>The following scenes described in your slides weren\'t found:</p>' +
                            '<ul>' + notFoundScenes.map(s => `<li>${s}</li>`).join('') + '</ul>' +
                            '<p>Available scenes are:</p>' +
                            '<ul>' + availableScenes.map(s => `<li>${s}</li>`).join('') + '</ul>' +
                            '</section>';

                        /* Allow moving across the scene list */
                        Reveal.getRevealElement().style["overflow"] = "inherit";
                        Reveal.getRevealElement().parentElement.style["overflow"] = "inherit";
                        Reveal.getRevealElement().parentElement.parentElement.style["overflow"] = "inherit";
                    }
                })
            });
            /* Send a change to OBS on any slide that contains a scene name */
            Reveal.addEventListener('slidechanged', event => {
                let scene = event.currentSlide.dataset.scene;
                if (scene !== undefined) {
                    obs.send('SetCurrentScene', {'scene-name': scene})
                }
            });
        }
    }

})();

Reveal.registerPlugin('obs', RevealOBS);