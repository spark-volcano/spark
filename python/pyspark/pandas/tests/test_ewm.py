#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
import numpy as np
import pandas as pd

import pyspark.pandas as ps
from pyspark.testing.pandasutils import PandasOnSparkTestCase, TestUtils
from pyspark.pandas.window import ExponentialMoving


class EWMTest(PandasOnSparkTestCase, TestUtils):
    def test_ewm_error(self):
        with self.assertRaisesRegex(
            TypeError, "psdf_or_psser must be a series or dataframe; however, got:.*int"
        ):
            ExponentialMoving(1, 2)

        psdf = ps.range(10)

        with self.assertRaisesRegex(ValueError, "min_periods must be >= 0"):
            psdf.ewm(min_periods=-1, alpha=0.5).mean()

        with self.assertRaisesRegex(ValueError, "com must be >= 0"):
            psdf.ewm(com=-0.1).mean()

        with self.assertRaisesRegex(ValueError, "span must be >= 1"):
            psdf.ewm(span=0.7).mean()

        with self.assertRaisesRegex(ValueError, "halflife must be > 0"):
            psdf.ewm(halflife=0).mean()

        with self.assertRaisesRegex(ValueError, "alpha must be in"):
            psdf.ewm(alpha=1.7).mean()

        with self.assertRaisesRegex(ValueError, "Must pass one of com, span, halflife, or alpha"):
            psdf.ewm().mean()

        with self.assertRaisesRegex(
            ValueError, "com, span, halflife, and alpha are mutually exclusive"
        ):
            psdf.ewm(com=0.5, alpha=0.7).mean()

    def _test_ewm_func(self, f):
        pser = pd.Series([1, 2, 3], index=np.random.rand(3), name="a")
        psser = ps.from_pandas(pser)
        self.assert_eq(getattr(psser.ewm(com=0.2), f)(), getattr(pser.ewm(com=0.2), f)())
        self.assert_eq(
            getattr(psser.ewm(com=0.2), f)().sum(), getattr(pser.ewm(com=0.2), f)().sum()
        )
        self.assert_eq(getattr(psser.ewm(span=1.7), f)(), getattr(pser.ewm(span=1.7), f)())
        self.assert_eq(
            getattr(psser.ewm(span=1.7), f)().sum(), getattr(pser.ewm(span=1.7), f)().sum()
        )
        self.assert_eq(getattr(psser.ewm(halflife=0.5), f)(), getattr(pser.ewm(halflife=0.5), f)())
        self.assert_eq(
            getattr(psser.ewm(halflife=0.5), f)().sum(), getattr(pser.ewm(halflife=0.5), f)().sum()
        )
        self.assert_eq(getattr(psser.ewm(alpha=0.7), f)(), getattr(pser.ewm(alpha=0.7), f)())
        self.assert_eq(
            getattr(psser.ewm(alpha=0.7), f)().sum(), getattr(pser.ewm(alpha=0.7), f)().sum()
        )
        self.assert_eq(
            getattr(psser.ewm(alpha=0.7, min_periods=2), f)(),
            getattr(pser.ewm(alpha=0.7, min_periods=2), f)(),
        )
        self.assert_eq(
            getattr(psser.ewm(alpha=0.7, min_periods=2), f)().sum(),
            getattr(pser.ewm(alpha=0.7, min_periods=2), f)().sum(),
        )

        pdf = pd.DataFrame(
            {"a": [1.0, 2.0, 3.0, 2.0], "b": [4.0, 2.0, 3.0, 1.0]}, index=np.random.rand(4)
        )
        psdf = ps.from_pandas(pdf)
        self.assert_eq(getattr(psdf.ewm(com=0.2), f)(), getattr(pdf.ewm(com=0.2), f)())
        self.assert_eq(getattr(psdf.ewm(com=0.2), f)().sum(), getattr(pdf.ewm(com=0.2), f)().sum())
        self.assert_eq(getattr(psdf.ewm(span=1.7), f)(), getattr(pdf.ewm(span=1.7), f)())
        self.assert_eq(
            getattr(psdf.ewm(span=1.7), f)().sum(), getattr(pdf.ewm(span=1.7), f)().sum()
        )
        self.assert_eq(getattr(psdf.ewm(halflife=0.5), f)(), getattr(pdf.ewm(halflife=0.5), f)())
        self.assert_eq(
            getattr(psdf.ewm(halflife=0.5), f)().sum(), getattr(pdf.ewm(halflife=0.5), f)().sum()
        )
        self.assert_eq(getattr(psdf.ewm(alpha=0.7), f)(), getattr(pdf.ewm(alpha=0.7), f)())
        self.assert_eq(
            getattr(psdf.ewm(alpha=0.7), f)().sum(), getattr(pdf.ewm(alpha=0.7), f)().sum()
        )
        self.assert_eq(
            getattr(psdf.ewm(alpha=0.7, min_periods=2), f)(),
            getattr(pdf.ewm(alpha=0.7, min_periods=2), f)(),
        )
        self.assert_eq(
            getattr(psdf.ewm(alpha=0.7, min_periods=2), f)().sum(),
            getattr(pdf.ewm(alpha=0.7, min_periods=2), f)().sum(),
        )

    def test_ewm_mean(self):
        self._test_ewm_func("mean")


if __name__ == "__main__":
    import unittest
    from pyspark.pandas.tests.test_ewm import *  # noqa: F401

    try:
        import xmlrunner  # type: ignore[import]

        testRunner = xmlrunner.XMLTestRunner(output="target/test-reports", verbosity=2)
    except ImportError:
        testRunner = None
    unittest.main(testRunner=testRunner, verbosity=2)
