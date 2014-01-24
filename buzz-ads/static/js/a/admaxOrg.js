(function(win, doc) {
	var dd = doc.documentElement;
	if ( !! win.admax) {
		return;
	}
	//crate a namespace;
	var admax = win.admax = {}, adUtil = admax.util = {
			/* JS Variable */
			encode: encodeURIComponent,
			isUndefined: function(test) {
				return typeof test == "undefined";
			},
			isArray: function(arr) {
				return Object.prototype.toString.call(arr).indexOf('Array') > 0;
			},
			extend: function(a, b) {
				for (var i in b) {
					if (typeof b[i] == "object") {
						if (!a[i] || a[i].length == 0) {
							a[i] = b[i];
						} else {
							if (adUtil.isArray(b[i])) {
								a[i].push(b[i].join(","));
							} else {
								adUtil.extend(a[i], b[i]);
							}
						}
					} else {
						a[i] = b[i];
					}
				}
				return a;
			},
			/* DOM */
			createElem: function(eTag, eId, eClass, eStyle, eText, eUrl) {
				// for internal use, should not document.
				var newEle = doc.createElement(eTag);
				if (eTag == "a") {
					newEle.href = eUrl || "javascript:;";
					newEle.target = "_blank";
				}
				if (eId) newEle.id = eId;
				if (eClass) newEle.className = eClass;
				if (eStyle) newEle.style.cssText = eStyle;
				if (eText) newEle.innerHTML = eText;
				return newEle;
			},
			getElemsByTN: function(element, tagName) {
				return element.getElementsByTagName(tagName);
			},
			getElemById: function(id) {
				return doc.getElementById(id);
			},
			addClass: function(ele, className) {
				if (!this.hasClass(ele, className)) {
					ele.className += (ele.className ? " " : "") + className;
				}
			},
			loadScript: function(params) {
				// params is an object, its properties are: url (string), callback (function, optional)
				var scriptEle = doc.createElement('script');
				scriptEle.src = params.url;
				scriptEle.type = 'text/javascript';
				scriptEle.charset = params.charset || 'utf-8';
				if (!adUtil.isUndefined(params.callback)) {
					scriptEle.onload = params.callback;
					scriptEle.onreadystatechange = function() {
						if (/complete|loaded/.test(this.readyState)) {
							params.callback();
						}
					};
				}
				(params.pnode || adUtil.getElemsByTN(doc, "head")[0]).appendChild(scriptEle);
			},
			loadStyle: function(css) {
				var styleElem = doc.createElement("style");
				styleElem.type = 'text/css';
				if (styleElem.styleSheet) {
					styleElem.styleSheet.cssText = css;
				} else {
					styleElem.appendChild(doc.createTextNode(css));
				}
				doc.getElementsByTagName("head")[0].appendChild(styleElem);
			},
			fixed: function(el, eltop, elleft) {
				//fix ie6 position:fixed bug
				win.onscroll = function() {
					el.style.top = (dd.scrollTop + eltop) + "px";
				}
			},
			getMeta: function(regex) {
				var meta = "";
				var metas = document.getElementsByTagName('meta');
				if ( !! metas) {
					for (var x = 0, y = metas.length; x < y; x++) {
						if (regex.test(metas[x].name)) {
							meta += metas[x].content;
						}
					}
				}
				return meta.length > 200 ? meta.substring(0, 200) : meta;
			},
			/* Process */
			ready: function(callback) {
				var done = false,
					top = true,
					add = doc.addEventListener ? 'addEventListener' : 'attachEvent',
					rem = doc.addEventListener ? 'removeEventListener' : 'detachEvent',
					pre = doc.addEventListener ? '' : 'on',
					init = function(e) {
						if (e.type == 'readystatechange' && doc.readyState != 'complete') return;
						(e.type == 'load' ? win : doc)[rem](pre + e.type, init, false);
						if (!done && (done = true)) callback.call(win, e.type || e);
					},
					poll = function() {
						try {
							dd.doScroll('left');
						} catch (e) {
							setTimeout(poll, 50);
							return;
						}
						init('poll');
					};
				if (doc.readyState == 'complete') callback.call(win, 'lazy');
				else {
					if (doc.createEventObject && dd.doScroll) {
						try {
							top = !win.frameElement;
						} catch (e) {}
						if (top) poll();
					}
					doc[add](pre + 'DOMContentLoaded', init, false);
					doc[add](pre + 'readystatechange', init, false);
					win[add](pre + 'load', init, false);
				}
			}
		};

	var statics = admax.statics = {
		ADMAX_STATIC_HOST: "http://static.buzzads.com",
		ADMAX_MAIN_HOST: "http://admax.buzzinate.com",
		ADMAX_SLOT_PREFIX: "ADMAX_"
	};
	statics.ADMAX_CLOSE_PATH = statics.ADMAX_STATIC_HOST + "/images/richc.gif";
	statics.ADMAX_ADS_PATH = statics.ADMAX_MAIN_HOST + "/ads";
	statics.ADMAX_SHOW_PATH = statics.ADMAX_MAIN_HOST + "/adsu";
	statics.ADMAX_CLICK_PATH = statics.ADMAX_MAIN_HOST + "/adck";

	admax = adUtil.extend(admax, {
		config: {
			uuid: "",
			slots: []
		},
		params: {},
		slotInfo: {},
		slotIslo: {}
	});

	admax.serve = {
		getAds: function(o) {
			admax.stats.prepare();
			var param = admax.params;
			if (param.sl == 0) return;
			var url = o.url + "?ts=" + param.ts + "&url=" + param.url + "&uuid=" + param.uuid + "&s=" + param.s +
				"&z=" + param.z + "&ti=" + param.ti + "&mk=" + param.mk + "&md=" + param.md + "&callback=" + o.callback;
			adUtil.loadScript({
				url: url
			});
		},
		callback: function(data) {
			//eid: response ads id
			/*
			 * open parmas api: win.buzzAds.params={...};
			 * uuid: This is confirm the user's identity ,that is obliged parameter
			 * slotId:  This is banner container id ,that is obliged parameter
			 * rt : This is resource type ,that has two values of parameter, 0 - 任意（默认），1 - 图片，2 - 文字，3 - Flash that is obliged parameter
			 * nw : This is network ,that has four values of parameter, 0:lezhi 1:bshare 2:buzzads 3:wjf 4:richmedia ,that is obliged parameter
			 * position: This is setting position of banner,currently,that only supports one value, 1: right_down
			 * width:This is setting width of banner
			 * height:This is setting height of banner
			 */
			if (data.success) {
				admax.render.fill(data.ads);
			}
		}
	};
	admax.render = {
		fill: function(ads) {
			// Fill the inventory
			var slots = admax.config.slots,
				i, length;
			for (i = 0, length = slots.length; i < length; ++i) {
				var sinfo = admax.slotInfo[ads[i].id],
					adData = adUtil.extend(ads[i], sinfo);
				if (sinfo.filled) continue;
				admax.render.create(adData);
				sinfo.filled = true;
				admax.stats.show({
					url: statics.ADMAX_SHOW_PATH,
					eid: adData.eid,
					nw: adData.nw
				});
			}
		},
		create: function(adData) {
			//create ads
			var container, adom, style, elem;
			container = adUtil.getElemById(statics.ADMAX_SLOT_PREFIX + adData.id);
			style = "width:" + adData.width + "px;height:" + adData.height + "px;line-height:0px;";
			//set position of banner
			if (!adData.position && adData.nw == "richmedia") { 
				adData.position = 1;
				adData.isClose = true;
			}
			switch (adData.position) {
				case 1:
					style += "bottom:0px;right:0px;";
					admax.render.loadRichCss(adData.id);
					break;
			}
			container.style.cssText = style;

			adom = adUtil.createElem("div");
			if (adData.isClose) {
				elem = adUtil.createElem("span", "buzzClose_" + adData.id, "buzzClose");
				adom.appendChild(elem);
				elem.onclick = function() {
					container.parentNode.removeChild(container);
				};
			}
			container.appendChild(adom);
			if (adData.thirdUrl) {
				elem = admax.render.third(adData);
			} else {
				switch (adData.rt) {
					case 1:
						elem = admax.render.banner(adData);
						break;
					case 2:
						elem = admax.render.flash(adData);
						break;
				}
			}
			adom.appendChild(elem);
			var downEvent = function() {
				admax.stats.click({
					url: statics.ADMAX_CLICK_PATH,
					eid: adData.eid,
					nw: adData.nw
				});
			}
			adom.onmousedown = downEvent;

			var isIE6 = !! win.ActiveXObject && !win.XMLHttpRequest;
			if (isIE6) {
				adUtil.fixed(container, (container.offsetTop - dd.scrollTop));
			}
		},
		flash: function(adData) {
			var newFO = adUtil.createElem("div", "", "admax_domwrap", "", '<object  id="adMaxFS_' + adData.id +
				'" type="application/x-shockwave-flash" width="' + adData.width + 'px" height="' + adData.height + 'px" data="' + adData.img +
				'">' +
				'<param name="movie" value="' + adData.img + '"/>' +
				'<param name="wmode" value="transparent"/>' +
				'<param name="allowScriptAccess" value="always"/>' +
				'<param name="allowNetworking" value="all"/>' +
				'</object>');
			return newFO;
		},
		banner: function(adData) {
			var link = adData.link ? adData.link.match((/\&(.*)/))[1].match(/\=(.*)/)[1] : "";
			var banner = adUtil.createElem("a", "", "", "", '<img class="" style="width:' + adData.width + 'px;height:' + adData.height + 'px;" src="' + adData.img +
				(adData.width > 120 ? "" : ('" title="' + adData.title)) + '"  />' +
				/* REMOVE START */
				(adData.width > 120 ? "" : ('<div class="feed-title" title="' + adData.title + '">' + adData.title + '</div>'))
				/* REMOVE END */
				, link);
			banner.onmousedown = function() {
				banner.href = adData.link;
				return true;
			}
			banner.onmouseup = function() {
				setTimeout(function() {
					banner.href = link;
				}, 50);
				return true;
			}
			return banner;
		},
		third: function(adData) {
			var csstemp = "height:" + adData.height + "px;width:" + adData.width + "px;border:0px;display: block;";
			var aDiv = adUtil.createElem("div", "", "", "", '<iframe id="" name="" frameborder="0" scrolling="no" allowtransparency="true" style="' + csstemp +
				'" src=' + adData.thirdUrl + '></iframe>');
			return aDiv;
		},
		loadRichCss: function(id) {
			adUtil.loadStyle("body{_background-image:url(about:blank);_background-attachment:fixed;}#" + statics.ADMAX_SLOT_PREFIX + id +
				"{position:fixed;_position:absolute;z-index:10000;}.buzzClose{position:absolute;background:url(" + statics.ADMAX_CLOSE_PATH + ") no-repeat 0px 0px;" +
				"display:inline-block;right:0px;width:9px;height:9px;text-align:center;cursor:pointer;}.buzzClose:hover{background-position:-11px 0px;}");
		}
	};
	admax.stats = {
		prepare: function() {
			//request ads
			var param = admax.params,
				slotes = admax.config.slots,
				s = [],
				i, length;
			for (i = 0, length = slotes.length; i < length; ++i) {
				if (admax.slotIslo[slotes[i]].load) {
					continue;
				}
				admax.slotIslo[slotes[i]].load = true;
				s.push(slotes[i]);
			}
			param.sl = s.length;
			if (s.length == 0) {
				return;
			}
			param.uuid = admax.config.uuid;
			param.url = adUtil.encode(doc.location.href);
			param.ts = (new Date()).getTime();
			param.z = parseInt(10000000000000000000 * Math.random()) + "";
			param.s = s.join(",");
			param.ti = /([^-_]*).*/.exec(doc.title)[1];
			param.mk = adUtil.getMeta(/^keywords$/i);
			param.md = adUtil.getMeta(/^description$/i);
		},
		show: function(option) {
			//count preview
			var params = admax.params;
			var timestamp = new Date().getTime();
			var url = option.url + "?ts=" + params.ts + "&url=" + params.url + "&uuid=" + params.uuid + "&z=" + params.z + "&nw=" +
				option.nw + "&eid=" + option.eid + "&timestamp=" + timestamp;
			adUtil.loadScript({
				url: url
			});
		},
		click: function(option) {
			//count click
			var params = admax.params;
			var url = option.url + "?ts=" + params.ts + "&url=" + params.url + "&uuid=" + params.uuid + "&z=" + params.z + "&nw=" + option.nw + "&eid=" + option.eid;
			adUtil.loadScript({
				url: url
			});
		}
	};
	admax.start = function() {
		admax.serve.getAds({
			url: statics.ADMAX_ADS_PATH,
			callback: "window.admax.serve.callback"
		});
	}
	admax.init = function() {
		// Parse options
		admax.config = adUtil.extend(admax.config, win.ADMAX_CONFIG || {});
		var slotes = admax.config.slots,
			i, length;
		if (slotes.length == 0) {
			return;
		}
		for (i = 0, length = slotes.length; i < length; ++i) {
			if (!admax.slotInfo[slotes[i]]) {
				admax.slotInfo[slotes[i]] = {
					filled: false
				};
			}
			if (!admax.slotIslo[slotes[i]]) {
				admax.slotIslo[slotes[i]] = {
					load: false
				}
			}
		}
		window.admax.util.ready(admax.start);
	};
})(window, document);
var reg=/Chrome|Safari|Mozilla|Opera|Firefox|MSIE|iPhone/i;
if(reg.test(navigator.userAgent)){
	window.admax.init();
}